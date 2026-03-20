package com.jonjam.weathermcp.hourlyforecast;

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
    builder = AccuWeatherHourlyTemperatureDto.AccuWeatherHourlyTemperatureDtoBuilder.class)
public class AccuWeatherHourlyTemperatureDto {

  @JsonProperty("Value")
  float value;

  @JsonProperty("Unit")
  String unit;
}
