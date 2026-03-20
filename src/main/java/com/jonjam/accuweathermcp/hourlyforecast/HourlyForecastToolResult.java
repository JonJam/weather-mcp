package com.jonjam.accuweathermcp.hourlyforecast;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HourlyForecastToolResult {

  String locationLocalizedName;

  String countryLocalizedName;

  List<HourlyForecastHourToolResult> hours;
}
