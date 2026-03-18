package com.jonjam.weathermcp.currentconditions;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface AccuWeatherCurrentConditionsClient {

  /**
   * Returns the current weather conditions for a specific location.
   *
   * @see <a
   *     href="https://developer.accuweather.com/current-conditions-api/apis/get/currentconditions/v1/%7BlocationKey%7D">
   *     AccuWeather Current Conditions API</a>
   */
  @GetExchange("/currentconditions/v1/{locationKey}")
  List<AccuWeatherCurrentConditionsDto> getCurrentConditions(
      @PathVariable("locationKey") String locationKey, @RequestParam("language") String language);
}
