package com.jonjam.accuweathermcp.currentconditions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = AccuWeatherTemperatureDto.AccuWeatherTemperatureDtoBuilder.class)
public class AccuWeatherTemperatureDto {

  @JsonProperty("Metric")
  AccuWeatherTemperatureUnitDto metric;

  @JsonProperty("Imperial")
  AccuWeatherTemperatureUnitDto imperial;
}
