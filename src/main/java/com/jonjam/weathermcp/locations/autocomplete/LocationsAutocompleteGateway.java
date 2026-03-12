package com.jonjam.weathermcp.locations.autocomplete;

import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationsAutocompleteGateway {

  private final AccuWeatherLocationsAutocompleteClient client;
  private final LocationSuggestionMapper locationSuggestionMapper;

  public List<LocationSuggestionDto> autocompleteForCitiesAndPointsOfInterest(
      final String partialName, final Locale language) {

    // TODO add error handling
    return client
        .autocompleteForCitiesAndPointsOfInterest(partialName, language.toLanguageTag())
        .stream()
        .map(locationSuggestionMapper::toLocationSuggestionDto)
        .toList();
  }
}
