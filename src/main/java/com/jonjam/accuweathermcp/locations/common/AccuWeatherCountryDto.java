package com.jonjam.accuweathermcp.locations.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = AccuWeatherCountryDto.AccuWeatherCountryDtoBuilder.class)
public class AccuWeatherCountryDto {
  @JsonProperty("ID")
  String id;

  @JsonProperty("LocalizedName")
  String localizedName;
}
