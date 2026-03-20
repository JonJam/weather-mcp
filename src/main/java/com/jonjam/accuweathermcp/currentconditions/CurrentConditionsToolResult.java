package com.jonjam.accuweathermcp.currentconditions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentConditionsToolResult {

  String locationLocalizedName;

  String countryLocalizedName;

  String localObservationDateTime;

  String weatherText;

  float temperatureMetric;

  float temperatureImperial;

  String link;
}
