package com.jonjam.weathermcp.locations.autocomplete;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface AccuWeatherLocationsAutocompleteClient {

  /**
   * Search for cities or points of interest around the world with an incomplete text string.
   *
   * @see <a
   *     href="https://developer.accuweather.com/core-weather/autocomplete#autocomplete-for-cities-and-points-of-interest>
   */
  @GetExchange("/locations/v1/autocomplete")
  List<AccuWeatherLocationsAutocompleteDto> autocompleteForCitiesAndPointsOfInterest(
      @RequestParam("q") String query, @RequestParam("language") String language);
}
