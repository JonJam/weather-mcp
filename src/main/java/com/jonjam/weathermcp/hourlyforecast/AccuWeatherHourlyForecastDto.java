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
@JsonDeserialize(builder = AccuWeatherHourlyForecastDto.AccuWeatherHourlyForecastDtoBuilder.class)
public class AccuWeatherHourlyForecastDto {

  @JsonProperty("DateTime")
  String dateTime;

  @JsonProperty("IconPhrase")
  String iconPhrase;

  @JsonProperty("Temperature")
  AccuWeatherHourlyTemperatureDto temperature;

  @JsonProperty("Link")
  String link;
}
