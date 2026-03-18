package com.jonjam.weathermcp.currentconditions;

import org.springframework.stereotype.Component;

@Component
public class CurrentConditionsMapper {

  public CurrentConditionsDto toCurrentConditionsDto(
      final AccuWeatherCurrentConditionsDto accuWeatherDto) {

    return CurrentConditionsDto.builder()
        .localObservationDateTime(accuWeatherDto.getLocalObservationDateTime())
        .weatherText(accuWeatherDto.getWeatherText())
        .temperatureMetric(accuWeatherDto.getTemperature().getMetric().getValue())
        .temperatureImperial(accuWeatherDto.getTemperature().getImperial().getValue())
        .link(accuWeatherDto.getLink())
        .build();
  }
}
