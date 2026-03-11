package com.jonjam.weathermcp;

import lombok.extern.apachecommons.CommonsLog;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

// TODO Remove
@Component
@CommonsLog
public class WeatherTools {

  @McpTool(name = "get_weather", description = "Get a simple weather summary for a given location.")
  public WeatherSummaryDto getWeather(String location) {

    log.info("Getting weather for location: " + location);

    return WeatherSummaryDto.builder()
        .location(location)
        .temperatureCelsius(21.5)
        .condition("Sunny")
        .build();
  }
}
