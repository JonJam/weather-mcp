package com.jonjam.weathermcp;

import com.jonjam.weathermcp.currentconditions.AccuWeatherCurrentConditionsClient;
import com.jonjam.weathermcp.dailyforecast.AccuWeatherDailyForecastsClient;
import com.jonjam.weathermcp.hourlyforecast.AccuWeatherHourlyForecastsClient;
import com.jonjam.weathermcp.locations.autocomplete.AccuWeatherLocationsAutocompleteClient;
import com.jonjam.weathermcp.locations.textsearch.AccuWeatherLocationTextSearchClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(
    group = "accuweather",
    types = {
      AccuWeatherLocationsAutocompleteClient.class,
      AccuWeatherLocationTextSearchClient.class,
      AccuWeatherCurrentConditionsClient.class,
      AccuWeatherDailyForecastsClient.class,
      AccuWeatherHourlyForecastsClient.class
    })
public class HttpClientConfiguration {}
