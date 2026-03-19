package com.jonjam.weathermcp.locations.autocomplete;

import com.jonjam.weathermcp.LocaleUtils;
import com.jonjam.weathermcp.Prompts;
import com.jonjam.weathermcp.locations.common.LocationSuggestionDto;
import com.jonjam.weathermcp.locations.common.LocationValidationUtils;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springaicommunity.mcp.annotation.McpComplete;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationsAutocompleteProvider {

  private final LocationsAutocompleteGateway gateway;

  @McpComplete(prompt = Prompts.CURRENT_CONDITIONS_PROMPT)
  public CompleteResult completeLocationForCurrentConditions(
      final @Nullable String partialLocation, final McpMeta meta) {

    return completeLocation(partialLocation, meta);
  }

  @McpComplete(prompt = Prompts.DAILY_FORECAST_PROMPT)
  public CompleteResult completeLocationForDailyForecast(
      final @Nullable String partialLocation, final McpMeta meta) {

    return completeLocation(partialLocation, meta);
  }

  private CompleteResult completeLocation(
      final @Nullable String partialLocation, final McpMeta meta) {

    final var normalizedLocationOptional =
        LocationValidationUtils.normalizeAndValidateLocation(partialLocation);

    if (normalizedLocationOptional.isEmpty()) {
      return new CompleteResult(
          new CompleteResult.CompleteCompletion(Collections.emptyList(), 0, false));
    }

    final String normalizedLocation = normalizedLocationOptional.orElseThrow();

    final Locale resolvedLanguage = LocaleUtils.resolveLocale(meta);

    final List<LocationSuggestionDto> suggestions =
        gateway.autocompleteForCitiesAndPointsOfInterest(normalizedLocation, resolvedLanguage);

    final List<String> results =
        suggestions.stream()
            .map(
                suggestion ->
                    String.format(
                        "%s, %s",
                        suggestion.getLocalizedName(), suggestion.getCountryLocalizedName()))
            .toList();

    return new CompleteResult(
        new CompleteResult.CompleteCompletion(results, results.size(), false));
  }
}
