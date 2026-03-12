package com.jonjam.weathermcp.locations.autocomplete;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface AccuWeatherLocationsAutocompleteClient {

  @GetExchange("/locations/v1/autocomplete")
  List<AccuWeatherLocationsAutocompleteDto> autocompleteForCitiesAndPointsOfInterest(
      @RequestParam("q") String query, @RequestParam("language") String language);
}
