package com.jonjam.accuweathermcp.hourlyforecast;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface AccuWeatherHourlyForecastsClient {

  /**
   * Returns 12 hours of hourly forecasts for a specific location.
   *
   * @see <a
   *     href="https://developer.accuweather.com/core-weather/location-key-hourly#12-hours-by-location-key">
   *     AccuWeather 12 Hours by Location Key</a>
   */
  @GetExchange("/forecasts/v1/hourly/12hour/{locationKey}")
  List<AccuWeatherHourlyForecastDto> getTwelveHoursByLocationKey(
      @PathVariable("locationKey") String locationKey,
      @RequestParam("language") String language,
      @RequestParam(value = "metric") boolean metric);
}
