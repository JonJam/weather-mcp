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
@JsonDeserialize(
    builder = AccuWeatherCurrentConditionsDto.AccuWeatherCurrentConditionsDtoBuilder.class)
public class AccuWeatherCurrentConditionsDto {

  @JsonProperty("LocalObservationDateTime")
  String localObservationDateTime;

  @JsonProperty("WeatherText")
  String weatherText;

  @JsonProperty("Temperature")
  AccuWeatherTemperatureDto temperature;

  @JsonProperty("Link")
  String link;
}
