package com.jonjam.weathermcp.hourlyforecast;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HourlyForecastToolResultMapper {

  public HourlyForecastToolResult toToolResult(
      final String locationLocalizedName,
      final String countryLocalizedName,
      final HourlyForecastSummaryDto summary) {

    final List<HourlyForecastHourToolResult> hours =
        summary.getHours().stream().map(this::toHourToolResult).toList();

    return HourlyForecastToolResult.builder()
        .locationLocalizedName(locationLocalizedName)
        .countryLocalizedName(countryLocalizedName)
        .hours(hours)
        .build();
  }

  private HourlyForecastHourToolResult toHourToolResult(final HourlyForecastHourSummaryDto hour) {

    return HourlyForecastHourToolResult.builder()
        .dateTime(hour.getDateTime())
        .iconPhrase(hour.getIconPhrase())
        .temperatureValue(hour.getTemperatureValue())
        .temperatureUnit(hour.getTemperatureUnit())
        .link(hour.getLink())
        .build();
  }
}
