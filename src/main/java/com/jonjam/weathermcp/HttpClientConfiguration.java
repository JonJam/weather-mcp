package com.jonjam.weathermcp;

import com.jonjam.weathermcp.locations.autocomplete.AccuWeatherLocationsAutocompleteClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "accuweather", types = AccuWeatherLocationsAutocompleteClient.class)
public class HttpClientConfiguration {}
