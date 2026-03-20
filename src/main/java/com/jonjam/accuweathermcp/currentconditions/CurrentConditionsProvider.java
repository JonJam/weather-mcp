package com.jonjam.accuweathermcp.currentconditions;

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
public class CurrentConditionsProvider {

  private final LocationsTextSearchGateway locationsTextSearchGateway;
  private final CurrentConditionsGateway currentConditionsGateway;
  private final CurrentConditionsToolResultMapper currentConditionsToolResultMapper;

  @McpPrompt(
      name = Prompts.CURRENT_CONDITIONS_PROMPT,
      description = "Ask for the current weather conditions in a location.")
  public String currentConditionsPrompt(
      @McpArg(name = "location", description = "City or point of interest", required = true)
          final String location) {

    final StringBuilder prompt = new StringBuilder("Provide the current weather conditions for ");

    prompt.append(location).append(".");

    return prompt.toString();
  }

  @McpTool(
      name = Prompts.CURRENT_CONDITIONS_PROMPT,
      description = "Get the current weather conditions for a location.",
      annotations =
          @McpTool.McpAnnotations(
              title = "Current Weather Conditions",
              readOnlyHint = true,
              destructiveHint = false,
              idempotentHint = true,
              openWorldHint = true))
  public CallToolResult currentConditionsTool(
      @McpToolParam(description = "City or point of interest", required = true)
          final String location,
      final McpSyncRequestContext context,
      final McpMeta meta) {

    context.info(String.format("Current conditions requested for raw location: %s", location));

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
        String.format(
            "Current conditions requested for normalized location: %s", normalizedLocation));

    final Locale resolvedLanguage = LocaleUtils.resolveLocale(meta);

    context.info(
        String.format("Requesting in resolved lanaguage: %s", resolvedLanguage.toLanguageTag()));

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
            "Looking up current conditions for location: id=%s, name=%s, country=%s",
            locationSuggestion.getId(),
            locationSuggestion.getLocalizedName(),
            locationSuggestion.getCountryLocalizedName()));

    final var currentConditionsOptional =
        currentConditionsGateway.getCurrentConditions(locationSuggestion.getId(), resolvedLanguage);

    if (currentConditionsOptional.isEmpty()) {
      return CallToolResult.builder()
          .isError(true)
          .addTextContent(
              String.format("Current conditions are not available for '%s'.", normalizedLocation))
          .build();
    }

    final var currentConditions = currentConditionsOptional.orElseThrow();

    context.info(
        String.format(
            "Current conditions found for location=%s; mapping tool result.",
            locationSuggestion.getId()));

    final CurrentConditionsToolResult toolResult =
        currentConditionsToolResultMapper.toToolResult(
            locationSuggestion.getLocalizedName(),
            locationSuggestion.getCountryLocalizedName(),
            currentConditions);

    context.info(
        String.format(
            "Returning current conditions tool result for: %s, %s",
            locationSuggestion.getLocalizedName(), locationSuggestion.getCountryLocalizedName()));

    final String formattedToolResult =
        String.format(
            "Location: %s, Country: %s, Temperature: %.1f°C (%.1f°F), Conditions: %s",
            toolResult.getLocationLocalizedName(),
            toolResult.getCountryLocalizedName(),
            toolResult.getTemperatureMetric(),
            toolResult.getTemperatureImperial(),
            toolResult.getWeatherText());

    return CallToolResult.builder()
        .addTextContent(formattedToolResult)
        .structuredContent(toolResult)
        .build();
  }
}
