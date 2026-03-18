package com.jonjam.weathermcp.locations.textsearch;

import com.jonjam.weathermcp.locations.common.LocationSuggestionDto;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationsTextSearchGateway {

  private final AccuWeatherLocationTextSearchClient client;
  private final LocationTextSearchMapper locationTextSearchMapper;

  /**
   * Performs a location search and returns the first matching result as a single suggestion, or
   * empty if no results.
   */
  public Optional<LocationSuggestionDto> search(final String query, final Locale language) {

    // TODO Add error handling
    return client.locationSearch(query, language.toLanguageTag()).stream()
        .findFirst()
        .map(locationTextSearchMapper::toLocationSuggestionDto);
  }
}
