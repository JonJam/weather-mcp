package com.jonjam.accuweathermcp.currentconditions;

import org.springframework.stereotype.Component;

@Component
public class CurrentConditionsToolResultMapper {

  public CurrentConditionsToolResult toToolResult(
      final String locationLocalizedName,
      final String countryLocalizedName,
      final CurrentConditionsDto currentConditionsDto) {

    return CurrentConditionsToolResult.builder()
        .locationLocalizedName(locationLocalizedName)
        .countryLocalizedName(countryLocalizedName)
        .localObservationDateTime(currentConditionsDto.getLocalObservationDateTime())
        .weatherText(currentConditionsDto.getWeatherText())
        .temperatureMetric(currentConditionsDto.getTemperatureMetric())
        .temperatureImperial(currentConditionsDto.getTemperatureImperial())
        .link(currentConditionsDto.getLink())
        .build();
  }
}
