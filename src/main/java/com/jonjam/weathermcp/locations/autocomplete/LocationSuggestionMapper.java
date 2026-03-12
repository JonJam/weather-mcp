package com.jonjam.weathermcp.locations.autocomplete;

import org.springframework.stereotype.Component;

@Component
public class LocationSuggestionMapper {

  public LocationSuggestionDto toLocationSuggestionDto(
      final AccuWeatherLocationsAutocompleteDto accuWeatherDto) {
    return LocationSuggestionDto.builder()
        .id(accuWeatherDto.getKey())
        .localizedName(accuWeatherDto.getLocalizedName())
        .countryKey(accuWeatherDto.getCountry().getId())
        .countryLocalizedName(accuWeatherDto.getCountry().getLocalizedName())
        .build();
  }
}
