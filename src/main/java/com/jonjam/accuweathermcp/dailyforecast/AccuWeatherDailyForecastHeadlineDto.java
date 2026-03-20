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
@JsonDeserialize(
    builder = AccuWeatherDailyForecastHeadlineDto.AccuWeatherDailyForecastHeadlineDtoBuilder.class)
public class AccuWeatherDailyForecastHeadlineDto {

  @JsonProperty("EffectiveDate")
  String effectiveDate;

  @JsonProperty("Text")
  String text;

  @JsonProperty("Link")
  String link;
}
