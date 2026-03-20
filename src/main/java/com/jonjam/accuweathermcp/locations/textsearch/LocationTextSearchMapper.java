package com.jonjam.accuweathermcp.locations.textsearch;

import com.jonjam.accuweathermcp.locations.common.LocationSuggestionDto;
import org.springframework.stereotype.Component;

@Component
public class LocationTextSearchMapper {

  public LocationSuggestionDto toLocationSuggestionDto(
      final AccuWeatherLocationSearchResultDto accuWeatherDto) {
    return LocationSuggestionDto.builder()
        .id(accuWeatherDto.getKey())
        .localizedName(accuWeatherDto.getLocalizedName())
        .countryKey(accuWeatherDto.getCountry().getId())
        .countryLocalizedName(accuWeatherDto.getCountry().getLocalizedName())
        .build();
  }
}
