package com.jonjam.weathermcp.locations.textsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jonjam.weathermcp.locations.common.AccuWeatherCountryDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(
    builder = AccuWeatherLocationSearchResultDto.AccuWeatherLocationSearchResultDtoBuilder.class)
public class AccuWeatherLocationSearchResultDto {

  @JsonProperty("Key")
  String key;

  @JsonProperty("Type")
  String type;

  @JsonProperty("LocalizedName")
  String localizedName;

  @JsonProperty("Country")
  AccuWeatherCountryDto country;
}
