package com.jonjam.accuweathermcp.dailyforecast;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyForecastDaySummaryDto {

  String date;

  float minimumTemperature;

  float maximumTemperature;

  String temperatureUnit;

  String daySummary;

  String nightSummary;
}
