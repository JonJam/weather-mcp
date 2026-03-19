package com.jonjam.weathermcp.dailyforecast;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface AccuWeatherDailyForecastsClient {

  /**
   * Returns five days of daily forecasts for a specific location.
   *
   * @see <a
   *     href="https://developer.accuweather.com/core-weather/location-key-daily#5-days-by-location-key">
   *     AccuWeather 5-Day Daily Forecast API</a>
   */
  @GetExchange("/forecasts/v1/daily/5day/{locationKey}")
  AccuWeatherDailyForecastsResponse getFiveDaysByLocationKey(
      @PathVariable("locationKey") String locationKey,
      @RequestParam("language") String language,
      @RequestParam(value = "metric") boolean metric);
}
