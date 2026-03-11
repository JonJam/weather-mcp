package com.jonjam.weathermcp.autocomplete;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpComplete;
import org.springframework.stereotype.Component;

// TODO Review
@Component
@RequiredArgsConstructor
public class LocationsAutocompleteProvider {

  private static final String PROMPT_NAME = "location-autocomplete";

  private final AutocompleteGateway gateway;

  @McpComplete(prompt = PROMPT_NAME)
  public List<String> completeLocation(String location) {
    return gateway.autocompleteForCitiesAndPointsOfInterest(location, null);
  }
}

