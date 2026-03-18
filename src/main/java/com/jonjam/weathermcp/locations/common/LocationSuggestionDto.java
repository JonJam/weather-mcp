package com.jonjam.weathermcp.locations.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationSuggestionDto {
  String id;

  String localizedName;

  String countryKey;

  String countryLocalizedName;
}
