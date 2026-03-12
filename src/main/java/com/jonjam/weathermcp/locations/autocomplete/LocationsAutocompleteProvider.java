package com.jonjam.weathermcp.locations.autocomplete;

import com.jonjam.weathermcp.LocaleUtils;
import com.jonjam.weathermcp.Prompts;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springaicommunity.mcp.annotation.McpComplete;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LocationsAutocompleteProvider {

  private final LocationsAutocompleteGateway gateway;

  @McpComplete(prompt = Prompts.CURRENT_CONDITIONS_PROMPT)
  public CompleteResult completeLocation(
      final @Nullable String partialLocation, final McpMeta meta) {

    final String trimmedPartialLocation = partialLocation != null ? partialLocation.trim() : null;

    if (!StringUtils.hasText(trimmedPartialLocation)
        || trimmedPartialLocation.length() < 3
        || trimmedPartialLocation.length() > 100) {
      return new CompleteResult(
          new CompleteResult.CompleteCompletion(Collections.emptyList(), 0, false));
    }

    final Locale resolvedLanguage = LocaleUtils.resolveLocale(meta);

    final List<LocationSuggestionDto> suggestions =
        gateway.autocompleteForCitiesAndPointsOfInterest(trimmedPartialLocation, resolvedLanguage);

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
