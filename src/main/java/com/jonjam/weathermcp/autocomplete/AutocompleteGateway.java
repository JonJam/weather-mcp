package com.jonjam.weathermcp.autocomplete;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutocompleteGateway {

  private static final String DEFAULT_LANGUAGE = "en-us";

  private final AccuWeatherAutocompleteClient client;

  public List<String> autocompleteForCitiesAndPointsOfInterest(
      String partialName, String language) {

    String query = Objects.requireNonNullElse(partialName, "").trim();
    if (query.isEmpty()) {
      return List.of();
    }

    String resolvedLanguage =
        (language == null || language.isBlank())
            ? DEFAULT_LANGUAGE
            : language.toLowerCase(Locale.ROOT);

    return client.autocompleteForCitiesAndPointsOfInterest(query, resolvedLanguage).stream()
        .map(AccuWeatherAutocompleteDto::getLocalizedName)
        .filter(name -> name != null && !name.isBlank())
        .distinct()
        .limit(100)
        .collect(Collectors.toList());
  }
}

