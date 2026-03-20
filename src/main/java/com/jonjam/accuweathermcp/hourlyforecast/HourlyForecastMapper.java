package com.jonjam.accuweathermcp.hourlyforecast;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HourlyForecastMapper {

  public HourlyForecastSummaryDto toHourlyForecastSummary(
      final List<AccuWeatherHourlyForecastDto> hourlyForecasts) {

    final List<HourlyForecastHourSummaryDto> hours =
        hourlyForecasts.stream().map(this::toHourSummary).toList();

    return HourlyForecastSummaryDto.builder().hours(hours).build();
  }

  private HourlyForecastHourSummaryDto toHourSummary(final AccuWeatherHourlyForecastDto hourly) {

    final AccuWeatherHourlyTemperatureDto temperature = hourly.getTemperature();

    return HourlyForecastHourSummaryDto.builder()
        .dateTime(hourly.getDateTime())
        .iconPhrase(hourly.getIconPhrase())
        .temperatureValue(temperature.getValue())
        .temperatureUnit(temperature.getUnit())
        .link(hourly.getLink())
        .build();
  }
}
