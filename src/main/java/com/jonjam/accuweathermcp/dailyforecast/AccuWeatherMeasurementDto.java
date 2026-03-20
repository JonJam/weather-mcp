package com.jonjam.accuweathermcp.dailyforecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = AccuWeatherMeasurementDto.AccuWeatherMeasurementDtoBuilder.class)
public class AccuWeatherMeasurementDto {

  @JsonProperty("Value")
  float value;

  @JsonProperty("Unit")
  String unit;
}
