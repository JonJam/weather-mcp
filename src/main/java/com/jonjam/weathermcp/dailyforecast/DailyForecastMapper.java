package com.jonjam.weathermcp.dailyforecast;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DailyForecastMapper {

  public DailyForecastSummaryDto toDailyForecastSummary(
      final AccuWeatherDailyForecastsResponse response) {

    final AccuWeatherDailyForecastHeadlineDto headline = response.getHeadline();
    final List<DailyForecastDaySummaryDto> days =
        response.getDailyForecasts().stream().map(this::toDaySummary).toList();

    return DailyForecastSummaryDto.builder()
        .headlineText(headline.getText())
        .headlineLink(headline.getLink())
        .days(days)
        .build();
  }

  private DailyForecastDaySummaryDto toDaySummary(final AccuWeatherDailyForecastDto day) {

    final AccuWeatherTemperatureRangeDto temperature = day.getTemperature();
    final AccuWeatherMeasurementDto minimum = temperature.getMinimum();
    final AccuWeatherMeasurementDto maximum = temperature.getMaximum();

    return DailyForecastDaySummaryDto.builder()
        .date(day.getDate())
        .minimumTemperature(minimum.getValue())
        .maximumTemperature(maximum.getValue())
        .temperatureUnit(minimum.getUnit())
        .daySummary(day.getDay().getIconPhrase())
        .nightSummary(day.getNight().getIconPhrase())
        .build();
  }
}
