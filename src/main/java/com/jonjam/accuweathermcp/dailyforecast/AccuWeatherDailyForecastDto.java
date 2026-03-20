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
@JsonDeserialize(builder = AccuWeatherDailyForecastDto.AccuWeatherDailyForecastDtoBuilder.class)
public class AccuWeatherDailyForecastDto {

  @JsonProperty("Date")
  String date;

  @JsonProperty("Temperature")
  AccuWeatherTemperatureRangeDto temperature;

  @JsonProperty("Day")
  AccuWeatherDailyForecastDayPartDto day;

  @JsonProperty("Night")
  AccuWeatherDailyForecastDayPartDto night;
}
