package com.jonjam.weathermcp.hourlyforecast;

import com.jonjam.weathermcp.LocaleUtils;
import com.jonjam.weathermcp.Prompts;
import com.jonjam.weathermcp.locations.common.LocationValidationUtils;
import com.jonjam.weathermcp.locations.textsearch.LocationsTextSearchGateway;
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
public class HourlyForecastProvider {

  private final LocationsTextSearchGateway locationsTextSearchGateway;
  private final HourlyForecastGateway hourlyForecastGateway;
  private final HourlyForecastToolResultMapper hourlyForecastToolResultMapper;

  @McpPrompt(
      name = Prompts.HOURLY_FORECAST_PROMPT,
      description = "Ask for the hourly weather forecast in a location.")
  public String hourlyForecastPrompt(
      @McpArg(name = "location", description = "City or point of interest", required = true)
          final String location) {

    final StringBuilder prompt = new StringBuilder("Provide the hourly weather forecast for ");

    prompt.append(location).append(".");

    return prompt.toString();
  }

  @McpTool(
      name = Prompts.HOURLY_FORECAST_PROMPT,
      description = "Get the 12-hour hourly weather forecast for a location.",
      annotations =
          @McpTool.McpAnnotations(
              title = "Hourly Weather Forecast",
              readOnlyHint = true,
              destructiveHint = false,
              idempotentHint = true,
              openWorldHint = true))
  public CallToolResult hourlyForecastTool(
      @McpToolParam(description = "City or point of interest", required = true)
          final String location,
      final McpSyncRequestContext context,
      final McpMeta meta) {

    context.info(String.format("Hourly forecast requested for raw location: %s", location));

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
        String.format("Hourly forecast requested for normalized location: %s", normalizedLocation));

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
            "Looking up hourly forecast for location: id=%s, name=%s, country=%s",
            locationSuggestion.getId(),
            locationSuggestion.getLocalizedName(),
            locationSuggestion.getCountryLocalizedName()));

    final var hourlyForecastOptional =
        hourlyForecastGateway.getHourlyForecastForTwelveHours(
            locationSuggestion.getId(), resolvedLanguage);

    if (hourlyForecastOptional.isEmpty()) {
      return CallToolResult.builder()
          .isError(true)
          .addTextContent(
              String.format("Hourly forecast is not available for '%s'.", normalizedLocation))
          .build();
    }

    final var hourlyForecastSummary = hourlyForecastOptional.orElseThrow();

    context.info(
        String.format(
            "Hourly forecast found for location=%s; mapping tool result.",
            locationSuggestion.getId()));

    final HourlyForecastToolResult toolResult =
        hourlyForecastToolResultMapper.toToolResult(
            locationSuggestion.getLocalizedName(),
            locationSuggestion.getCountryLocalizedName(),
            hourlyForecastSummary);

    context.info(
        String.format(
            "Returning hourly forecast tool result for: %s, %s",
            locationSuggestion.getLocalizedName(), locationSuggestion.getCountryLocalizedName()));

    final String formattedToolResult = formatHourlyForecastText(toolResult);

    return CallToolResult.builder()
        .addTextContent(formattedToolResult)
        .structuredContent(toolResult)
        .build();
  }

  private static String formatHourlyForecastText(final HourlyForecastToolResult toolResult) {

    final StringBuilder text = new StringBuilder();
    text.append(
        String.format(
            "Location: %s, Country: %s%n",
            toolResult.getLocationLocalizedName(), toolResult.getCountryLocalizedName()));

    for (final HourlyForecastHourToolResult hour : toolResult.getHours()) {
      text.append(
          String.format(
              "%s: %.1f°%s, %s. More detail: %s.%n",
              hour.getDateTime(),
              hour.getTemperatureValue(),
              hour.getTemperatureUnit(),
              hour.getIconPhrase(),
              hour.getLink()));
    }

    return text.toString();
  }
}
