package com.jonjam.weathermcp.dailyforecast;

import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyForecastGateway {

  private final AccuWeatherDailyForecastsClient client;
  private final DailyForecastMapper dailyForecastMapper;

  public Optional<DailyForecastSummaryDto> getFiveDayForecast(
      final String locationKey, final Locale language) {

    // TODO Add error handling
    final AccuWeatherDailyForecastsResponse response =
        client.getFiveDaysByLocationKey(locationKey, language.toLanguageTag(), true);

    return Optional.of(dailyForecastMapper.toDailyForecastSummary(response));
  }
}
