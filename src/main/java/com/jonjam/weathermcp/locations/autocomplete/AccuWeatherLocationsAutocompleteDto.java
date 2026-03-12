package com.jonjam.weathermcp.locations.autocomplete;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(
    builder = AccuWeatherLocationsAutocompleteDto.AccuWeatherLocationsAutocompleteDtoBuilder.class)
public class AccuWeatherLocationsAutocompleteDto {

  @JsonProperty("Key")
  String key;

  @JsonProperty("LocalizedName")
  String localizedName;

  @JsonProperty("Type")
  String type;

  @JsonProperty("Country")
  AccuWeatherCountryDto country;
}
