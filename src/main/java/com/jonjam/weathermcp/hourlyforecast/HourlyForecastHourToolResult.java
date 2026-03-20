package com.jonjam.weathermcp.hourlyforecast;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HourlyForecastHourToolResult {

  String dateTime;

  String iconPhrase;

  float temperatureValue;

  String temperatureUnit;
}
