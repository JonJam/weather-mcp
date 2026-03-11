package com.jonjam.weathermcp.autocomplete;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AccuWeatherAutocompleteClient {

  private final RestClient accuweatherRestClient;

  public List<AccuWeatherAutocompleteDto> autocompleteForCitiesAndPointsOfInterest(
      String query, String language) {

    return accuweatherRestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/locations/v1/autocomplete")
                    .queryParam("q", query)
                    .queryParam("language", language)
                    .build())
        .retrieve()
        .body(new ParameterizedTypeReference<List<AccuWeatherAutocompleteDto>>() {});
  }
}

