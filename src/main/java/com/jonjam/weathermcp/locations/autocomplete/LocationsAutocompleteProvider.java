package com.jonjam.weathermcp.locations.autocomplete;

import com.jonjam.weathermcp.LocaleUtils;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpComplete;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationsAutocompleteProvider {

  private final LocationsAutocompleteGateway gateway;

  // TODO Create constant for prompt name
  @McpComplete(prompt = "current-conditions")
  public CompleteResult completeLocation(final String partialLocation, final McpMeta meta) {

    // TODO Validation partialLocation (empty, null, min length, max length, etc.)

    final Locale resolvedLanguage = LocaleUtils.resolveLocale(meta);

    final List<LocationSuggestionDto> suggestions =
        gateway.autocompleteForCitiesAndPointsOfInterest(partialLocation, resolvedLanguage);

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
