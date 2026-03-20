package com.jonjam.accuweathermcp.locations.textsearch;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface AccuWeatherLocationTextSearchClient {

  /**
   * Specify a city name, postal code or point of interest to return location data.
   *
   * @see <a
   *     href="https://developer.accuweather.com/core-weather/text-search#location-search">AccuWeather
   *     Location Search API</a>
   */
  @GetExchange("/locations/v1/search")
  List<AccuWeatherLocationSearchResultDto> locationSearch(
      @RequestParam("q") String query, @RequestParam("language") String language);
}
