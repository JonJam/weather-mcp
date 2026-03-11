package com.jonjam.weathermcp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

// TOOD Remove
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherSummaryDto {
  String location;
  double temperatureCelsius;
  String condition;
}
