package com.jonjam.accuweathermcp;

import com.jonjam.accuweathermcp.currentconditions.AccuWeatherCurrentConditionsClient;
import com.jonjam.accuweathermcp.dailyforecast.AccuWeatherDailyForecastsClient;
import com.jonjam.accuweathermcp.hourlyforecast.AccuWeatherHourlyForecastsClient;
import com.jonjam.accuweathermcp.locations.autocomplete.AccuWeatherLocationsAutocompleteClient;
import com.jonjam.accuweathermcp.locations.textsearch.AccuWeatherLocationTextSearchClient;
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
