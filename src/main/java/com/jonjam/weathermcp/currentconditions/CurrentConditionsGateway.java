package com.jonjam.weathermcp.currentconditions;

import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentConditionsGateway {

  private final AccuWeatherCurrentConditionsClient client;
  private final CurrentConditionsMapper currentConditionsMapper;

  /**
   * Returns the current weather conditions for the given AccuWeather location key.
   *
   * <p>If AccuWeather returns no results, an empty {@link Optional} is returned.
   */
  public Optional<CurrentConditionsDto> getCurrentConditions(
      final String locationKey, final Locale language) {

    // TODO Add error handling
    return client.getCurrentConditions(locationKey, language.toLanguageTag()).stream()
        .findFirst()
        .map(currentConditionsMapper::toCurrentConditionsDto);
  }
}
