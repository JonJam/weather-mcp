package com.jonjam.weathermcp.dailyforecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(
    builder = AccuWeatherDailyForecastsResponse.AccuWeatherDailyForecastsResponseBuilder.class)
public class AccuWeatherDailyForecastsResponse {

  @JsonProperty("Headline")
  AccuWeatherDailyForecastHeadlineDto headline;

  @JsonProperty("DailyForecasts")
  List<AccuWeatherDailyForecastDto> dailyForecasts;
}
