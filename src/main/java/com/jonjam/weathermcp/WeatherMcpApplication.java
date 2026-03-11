package com.jonjam.weathermcp;

import com.jonjam.weathermcp.autocomplete.AccuWeatherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = AccuWeatherProperties.class)
public class WeatherMcpApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherMcpApplication.class, args);
  }
}
