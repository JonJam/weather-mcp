package com.jonjam.weathermcp.locations.autocomplete;

import com.jonjam.weathermcp.Constants;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpComplete;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LocationsAutocompleteProvider {

  private final LocationsAutocompleteGateway gateway;

  // TODO verify if MCP arg works
  @McpComplete(prompt = "location-search")
  public CompleteResult completeLocation(
      @McpArg(
              name = "partialLocation",
              description = "Partial location to autocomplete",
              required = true)
          final String partialLocation,
      @McpArg(name = "language", description = "Language to use for autocomplete") @Nullable
          final String language) {

    // TODO validate input
    final Locale resolvedLanguage =
        StringUtils.hasText(language)
            ? Locale.forLanguageTag(language)
            : Constants.DEFAULT_LANGUAGE;

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
