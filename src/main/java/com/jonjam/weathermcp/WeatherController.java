package com.jonjam.weathermcp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@CommonsLog
public class WeatherController {

  @GetMapping("/weather")
  public WeatherSummaryDto getWeather(@RequestParam String location) {

	log.info("Getting weather for location: " + location);
	
    return WeatherSummaryDto.builder()
        .location(location)
        .temperatureCelsius(21.5)
        .condition("Sunny")
        .build();
  }
}
