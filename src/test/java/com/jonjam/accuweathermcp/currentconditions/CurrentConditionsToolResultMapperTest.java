package com.jonjam.accuweathermcp.currentconditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CurrentConditionsToolResultMapper")
class CurrentConditionsToolResultMapperTest {

  private final CurrentConditionsToolResultMapper mapper = new CurrentConditionsToolResultMapper();

  @Test
  @DisplayName("maps CurrentConditionsDto to CurrentConditionsToolResult")
  void mapsCurrentConditionsDtoToToolResult() {
    // Arrange
    final CurrentConditionsDto source =
        CurrentConditionsDto.builder()
            .localObservationDateTime("2024-01-01T12:00:00-08:00")
            .weatherText("Sunny")
            .temperatureMetric(20L)
            .temperatureImperial(68L)
            .link("https://example.com/current-conditions")
            .build();

    final String locationLocalizedName = "San Francisco";
    final String countryLocalizedName = "United States";

    // Act
    final CurrentConditionsToolResult result =
        mapper.toToolResult(locationLocalizedName, countryLocalizedName, source);

    // Assert
    assertThat(result.getLocationLocalizedName(), is(locationLocalizedName));
    assertThat(result.getCountryLocalizedName(), is(countryLocalizedName));
    assertThat(result.getLocalObservationDateTime(), is(source.getLocalObservationDateTime()));
    assertThat(result.getWeatherText(), is(source.getWeatherText()));
    assertThat(result.getTemperatureMetric(), is(source.getTemperatureMetric()));
    assertThat(result.getTemperatureImperial(), is(source.getTemperatureImperial()));
    assertThat(result.getLink(), is(source.getLink()));
  }
}
