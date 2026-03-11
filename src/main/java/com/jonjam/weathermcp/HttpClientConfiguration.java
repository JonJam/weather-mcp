package com.jonjam.weathermcp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

import com.jonjam.weathermcp.autocomplete.AccuWeatherLocationsAutocompleteClient;

@Configuration
@ImportHttpServices(group = "accuweather", types = AccuWeatherLocationsAutocompleteClient.class)
public class HttpClientConfiguration {
}
