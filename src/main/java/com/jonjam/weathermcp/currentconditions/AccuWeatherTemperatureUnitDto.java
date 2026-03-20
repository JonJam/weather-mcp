package com.jonjam.weathermcp.currentconditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = AccuWeatherTemperatureUnitDto.AccuWeatherTemperatureUnitDtoBuilder.class)
public class AccuWeatherTemperatureUnitDto {

  @JsonProperty("Value")
  float value;

  @JsonProperty("Unit")
  String unit;
}
