package com.jonjam.weathermcp.currentconditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CurrentConditionsMapper")
class CurrentConditionsMapperTest {

  private final CurrentConditionsMapper mapper = new CurrentConditionsMapper();

  @Nested
  @DisplayName("toCurrentConditionsDto")
  class ToCurrentConditionsDto {

    @Test
    @DisplayName("maps all fields from AccuWeather DTO")
    void mapsAllFieldsFromAccuWeatherDto() {
      // Arrange
      final AccuWeatherTemperatureUnitDto metric =
          AccuWeatherTemperatureUnitDto.builder().value(20L).unit("C").build();
      final AccuWeatherTemperatureUnitDto imperial =
          AccuWeatherTemperatureUnitDto.builder().value(68L).unit("F").build();
      final AccuWeatherTemperatureDto temperature =
          AccuWeatherTemperatureDto.builder().metric(metric).imperial(imperial).build();

      final AccuWeatherCurrentConditionsDto accuWeatherDto =
          AccuWeatherCurrentConditionsDto.builder()
              .localObservationDateTime("2024-01-01T12:00:00-08:00")
              .weatherText("Sunny")
              .temperature(temperature)
              .link("https://example.com/current-conditions")
              .build();

      // Act
      final CurrentConditionsDto result = mapper.toCurrentConditionsDto(accuWeatherDto);

      // Assert
      assertThat(result.getLocalObservationDateTime(), is("2024-01-01T12:00:00-08:00"));
      assertThat(result.getWeatherText(), is("Sunny"));
      assertThat(result.getTemperatureMetric(), is(20f));
      assertThat(result.getTemperatureImperial(), is(68f));
      assertThat(result.getLink(), is("https://example.com/current-conditions"));
    }
  }
}
