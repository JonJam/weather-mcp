package com.jonjam.weathermcp.hourlyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HourlyForecastMapper")
class HourlyForecastMapperTest {

  private final HourlyForecastMapper mapper = new HourlyForecastMapper();

  @Nested
  @DisplayName("toHourlyForecastSummary")
  class ToHourlyForecastSummary {

    @Test
    @DisplayName("maps AccuWeather hourly list to summary with two hours")
    void mapsAccuWeatherHourlyListToSummaryWithTwoHours() {
      // Arrange
      final List<AccuWeatherHourlyForecastDto> hourlyForecasts =
          List.of(
              AccuWeatherHourlyForecastDto.builder()
                  .dateTime("2026-03-19T14:00:00+00:00")
                  .iconPhrase("Intermittent clouds")
                  .temperature(
                      AccuWeatherHourlyTemperatureDto.builder()
                          .value(18.5f)
                          .unit("C")
                          .unitType(17)
                          .build())
                  .link("https://www.accuweather.com/en/es/valencia/352579/hourly")
                  .build(),
              AccuWeatherHourlyForecastDto.builder()
                  .dateTime("2026-03-19T15:00:00+00:00")
                  .iconPhrase("Partly sunny")
                  .temperature(
                      AccuWeatherHourlyTemperatureDto.builder()
                          .value(19.2f)
                          .unit("C")
                          .unitType(17)
                          .build())
                  .link("https://www.accuweather.com/en/es/valencia/352579/hourly?hour=15")
                  .build());

      // Act
      final HourlyForecastSummaryDto summary = mapper.toHourlyForecastSummary(hourlyForecasts);

      // Assert
      assertThat(
          summary.getDetailLink(), is("https://www.accuweather.com/en/es/valencia/352579/hourly"));
      assertThat(summary.getHours(), hasSize(2));

      final HourlyForecastHourSummaryDto firstHour = summary.getHours().get(0);
      assertThat(firstHour.getDateTime(), is("2026-03-19T14:00:00+00:00"));
      assertThat(firstHour.getIconPhrase(), is("Intermittent clouds"));
      assertThat(firstHour.getTemperatureValue(), is(18.5f));
      assertThat(firstHour.getTemperatureUnit(), is("C"));

      final HourlyForecastHourSummaryDto secondHour = summary.getHours().get(1);
      assertThat(secondHour.getDateTime(), is("2026-03-19T15:00:00+00:00"));
      assertThat(secondHour.getIconPhrase(), is("Partly sunny"));
      assertThat(secondHour.getTemperatureValue(), is(19.2f));
      assertThat(secondHour.getTemperatureUnit(), is("C"));
    }

    @Test
    @DisplayName("uses first hour link as detailLink when present")
    void usesFirstHourLinkAsDetailLinkWhenPresent() {
      // Arrange
      final List<AccuWeatherHourlyForecastDto> hourlyForecasts =
          List.of(
              AccuWeatherHourlyForecastDto.builder()
                  .dateTime("2026-03-19T14:00:00+00:00")
                  .iconPhrase("Clear")
                  .temperature(
                      AccuWeatherHourlyTemperatureDto.builder()
                          .value(20.0f)
                          .unit("C")
                          .unitType(17)
                          .build())
                  .link("https://example.com/first")
                  .build());

      // Act
      final HourlyForecastSummaryDto summary = mapper.toHourlyForecastSummary(hourlyForecasts);

      // Assert
      assertThat(summary.getDetailLink(), is("https://example.com/first"));
    }

    @Test
    @DisplayName("returns null detailLink when list is empty")
    void returnsNullDetailLinkWhenListIsEmpty() {
      // Arrange
      final List<AccuWeatherHourlyForecastDto> hourlyForecasts = List.of();

      // Act
      final HourlyForecastSummaryDto summary = mapper.toHourlyForecastSummary(hourlyForecasts);

      // Assert
      assertThat(summary.getDetailLink(), is(nullValue()));
      assertThat(summary.getHours(), hasSize(0));
    }
  }
}
