package com.jonjam.accuweathermcp.dailyforecast;

import com.jonjam.accuweathermcp.LocaleUtils;
import com.jonjam.accuweathermcp.Prompts;
import com.jonjam.accuweathermcp.locations.common.LocationValidationUtils;
import com.jonjam.accuweathermcp.locations.textsearch.LocationsTextSearchGateway;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springaicommunity.mcp.context.McpSyncRequestContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyForecastProvider {

  private final LocationsTextSearchGateway locationsTextSearchGateway;
  private final DailyForecastGateway dailyForecastGateway;
  private final DailyForecastToolResultMapper dailyForecastToolResultMapper;

  @McpPrompt(
      name = Prompts.DAILY_FORECAST_PROMPT,
      description = "Ask for the daily weather forecast in a location.")
  public String dailyForecastPrompt(
      @McpArg(name = "location", description = "City or point of interest", required = true)
          final String location) {

    final StringBuilder prompt = new StringBuilder("Provide the daily weather forecast for ");

    prompt.append(location).append(".");

    return prompt.toString();
  }

  @McpTool(
      name = Prompts.DAILY_FORECAST_PROMPT,
      description = "Get the daily weather forecast for a location.",
      annotations =
          @McpTool.McpAnnotations(
              title = "Daily Weather Forecast",
              readOnlyHint = true,
              destructiveHint = false,
              idempotentHint = true,
              openWorldHint = true))
  public CallToolResult dailyForecastTool(
      @McpToolParam(description = "City or point of interest", required = true)
          final String location,
      final McpSyncRequestContext context,
      final McpMeta meta) {

    context.info(String.format("Daily forecast requested for raw location: %s", location));

    final var normalizedLocationOptional =
        LocationValidationUtils.normalizeAndValidateLocation(location);

    if (normalizedLocationOptional.isEmpty()) {
      return CallToolResult.builder()
          .isError(true)
          .addTextContent(
              "The 'location' parameter must be between 3 and 100 characters of non-blank text.")
          .build();
    }

    final String normalizedLocation = normalizedLocationOptional.orElseThrow();

    context.info(
        String.format("Daily forecast requested for normalized location: %s", normalizedLocation));

    final Locale resolvedLanguage = LocaleUtils.resolveLocale(meta);

    context.info(
        String.format("Requesting in resolved language: %s", resolvedLanguage.toLanguageTag()));

    final var locationSuggestionOptional =
        locationsTextSearchGateway.search(normalizedLocation, resolvedLanguage);

    if (locationSuggestionOptional.isEmpty()) {
      return CallToolResult.builder()
          .isError(true)
          .addTextContent(
              String.format("No locations were found matching '%s'.", normalizedLocation))
          .build();
    }

    final var locationSuggestion = locationSuggestionOptional.orElseThrow();

    context.info(
        String.format(
            "Looking up daily forecast for location: id=%s, name=%s, country=%s",
            locationSuggestion.getId(),
            locationSuggestion.getLocalizedName(),
            locationSuggestion.getCountryLocalizedName()));

    final var dailyForecastOptional =
        dailyForecastGateway.getFiveDayForecast(locationSuggestion.getId(), resolvedLanguage);

    if (dailyForecastOptional.isEmpty()) {
      return CallToolResult.builder()
          .isError(true)
          .addTextContent(
              String.format("Daily forecast is not available for '%s'.", normalizedLocation))
          .build();
    }

    final var dailyForecastSummary = dailyForecastOptional.orElseThrow();

    context.info(
        String.format(
            "Daily forecast found for location=%s; mapping tool result.",
            locationSuggestion.getId()));

    final DailyForecastToolResult toolResult =
        dailyForecastToolResultMapper.toToolResult(
            locationSuggestion.getLocalizedName(),
            locationSuggestion.getCountryLocalizedName(),
            dailyForecastSummary);

    context.info(
        String.format(
            "Returning daily forecast tool result for: %s, %s",
            locationSuggestion.getLocalizedName(), locationSuggestion.getCountryLocalizedName()));

    final String formattedToolResult = formatDailyForecastText(toolResult);

    return CallToolResult.builder()
        .addTextContent(formattedToolResult)
        .structuredContent(toolResult)
        .build();
  }

  private static String formatDailyForecastText(final DailyForecastToolResult toolResult) {

    final StringBuilder text = new StringBuilder();
    text.append(
        String.format(
            "Location: %s, Country: %s%nHeadline: %s%n",
            toolResult.getLocationLocalizedName(),
            toolResult.getCountryLocalizedName(),
            toolResult.getHeadlineText()));

    for (final DailyForecastDayToolResult day : toolResult.getDays()) {
      text.append(
          String.format(
              "%s: Low %.1f°%s High %.1f°%s, Day: %s, Night: %s%n",
              day.getDate(),
              day.getMinimumTemperature(),
              day.getTemperatureUnit(),
              day.getMaximumTemperature(),
              day.getTemperatureUnit(),
              day.getDaySummary(),
              day.getNightSummary()));
    }

    text.append(String.format("More detail: %s", toolResult.getHeadlineLink()));

    return text.toString();
  }
}
