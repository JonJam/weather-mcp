package com.jonjam.weathermcp.hourlyforecast;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HourlyForecastMapper {

  /**
   * Maps AccuWeather hourly forecast list to domain summary. Uses the first hour's Link as
   * detailLink when present, for text output and user navigation.
   */
  public HourlyForecastSummaryDto toHourlyForecastSummary(
      final List<AccuWeatherHourlyForecastDto> hourlyForecasts) {

    final List<HourlyForecastHourSummaryDto> hours =
        hourlyForecasts.stream().map(this::toHourSummary).toList();

    final String detailLink = hourlyForecasts.isEmpty() ? null : hourlyForecasts.get(0).getLink();

    return HourlyForecastSummaryDto.builder().detailLink(detailLink).hours(hours).build();
  }

  private HourlyForecastHourSummaryDto toHourSummary(final AccuWeatherHourlyForecastDto hourly) {

    final AccuWeatherHourlyTemperatureDto temperature = hourly.getTemperature();

    return HourlyForecastHourSummaryDto.builder()
        .dateTime(hourly.getDateTime())
        .iconPhrase(hourly.getIconPhrase())
        .temperatureValue(temperature.getValue())
        .temperatureUnit(temperature.getUnit())
        .build();
  }
}
