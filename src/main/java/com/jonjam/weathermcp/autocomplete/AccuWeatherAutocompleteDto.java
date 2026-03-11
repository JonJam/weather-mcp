package com.jonjam.weathermcp.autocomplete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AccuWeatherAutocompleteDto {

  @JsonProperty("Key")
  String key;

  @JsonProperty("LocalizedName")
  String localizedName;

  @JsonProperty("Type")
  String type;
}

