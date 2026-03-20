package com.jonjam.accuweathermcp.locations.autocomplete;

import com.jonjam.accuweathermcp.locations.common.LocationSuggestionDto;
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
