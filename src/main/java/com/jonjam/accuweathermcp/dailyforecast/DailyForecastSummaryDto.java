package com.jonjam.accuweathermcp.dailyforecast;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyForecastSummaryDto {

  String headlineText;

  String headlineLink;

  List<DailyForecastDaySummaryDto> days;
}
