package com.jonjam.weathermcp.dailyforecast;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DailyForecastToolResultMapper {

  public DailyForecastToolResult toToolResult(
      final String locationLocalizedName,
      final String countryLocalizedName,
      final DailyForecastSummaryDto summary) {

    final List<DailyForecastDayToolResult> days =
        summary.getDays().stream().map(this::toDayToolResult).toList();

    return DailyForecastToolResult.builder()
        .locationLocalizedName(locationLocalizedName)
        .countryLocalizedName(countryLocalizedName)
        .headlineText(summary.getHeadlineText())
        .headlineLink(summary.getHeadlineLink())
        .days(days)
        .build();
  }

  private DailyForecastDayToolResult toDayToolResult(final DailyForecastDaySummaryDto day) {

    return DailyForecastDayToolResult.builder()
        .date(day.getDate())
        .minimumTemperature(day.getMinimumTemperature())
        .maximumTemperature(day.getMaximumTemperature())
        .temperatureUnit(day.getTemperatureUnit())
        .daySummary(day.getDaySummary())
        .nightSummary(day.getNightSummary())
        .build();
  }
}
