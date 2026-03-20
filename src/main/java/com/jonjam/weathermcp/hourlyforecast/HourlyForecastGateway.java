package com.jonjam.weathermcp.hourlyforecast;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HourlyForecastGateway {

  private final AccuWeatherHourlyForecastsClient client;
  private final HourlyForecastMapper hourlyForecastMapper;

  public Optional<HourlyForecastSummaryDto> getHourlyForecastForTwelveHours(
      final String locationKey, final Locale language) {

    // TODO Add error handling
    final List<AccuWeatherHourlyForecastDto> hourlyForecasts =
        client.getTwelveHoursByLocationKey(locationKey, language.toLanguageTag(), true);

    return Optional.of(hourlyForecastMapper.toHourlyForecastSummary(hourlyForecasts));
  }
}
