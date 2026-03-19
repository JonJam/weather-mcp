package com.jonjam.weathermcp.dailyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DailyForecastMapper")
class DailyForecastMapperTest {

  private final DailyForecastMapper mapper = new DailyForecastMapper();

  @Nested
  @DisplayName("toDailyForecastSummary")
  class ToDailyForecastSummary {

    @Test
    @DisplayName("maps AccuWeather response to flattened summary with one day")
    void mapsAccuWeatherResponseToFlattenedSummaryWithOneDay() {
      // Arrange
      final AccuWeatherDailyForecastsResponse response =
          AccuWeatherDailyForecastsResponse.builder()
              .headline(
                  AccuWeatherDailyForecastHeadlineDto.builder()
                      .effectiveDate("2026-01-01T07:00:00+00:00")
                      .text("Mild this week")
                      .link("https://www.accuweather.com/example")
                      .build())
              .dailyForecasts(
                  List.of(
                      AccuWeatherDailyForecastDto.builder()
                          .date("2026-01-02T07:00:00+00:00")
                          .temperature(
                              AccuWeatherTemperatureRangeDto.builder()
                                  .minimum(
                                      AccuWeatherMeasurementDto.builder()
                                          .value(3.5f)
                                          .unit("C")
                                          .build())
                                  .maximum(
                                      AccuWeatherMeasurementDto.builder()
                                          .value(9.0f)
                                          .unit("C")
                                          .build())
                                  .build())
                          .day(
                              AccuWeatherDailyForecastDayPartDto.builder()
                                  .iconPhrase("Partly sunny")
                                  .build())
                          .night(
                              AccuWeatherDailyForecastDayPartDto.builder()
                                  .iconPhrase("Clear")
                                  .build())
                          .build()))
              .build();

      // Act
      final DailyForecastSummaryDto summary = mapper.toDailyForecastSummary(response);

      // Assert
      assertThat(summary.getHeadlineText(), is("Mild this week"));
      assertThat(summary.getHeadlineLink(), is("https://www.accuweather.com/example"));
      assertThat(summary.getDays(), hasSize(1));

      final DailyForecastDaySummaryDto day = summary.getDays().get(0);
      assertThat(day.getDate(), is("2026-01-02T07:00:00+00:00"));
      assertThat(day.getMinimumTemperature(), is(3.5f));
      assertThat(day.getMaximumTemperature(), is(9.0f));
      assertThat(day.getTemperatureUnit(), is("C"));
      assertThat(day.getDaySummary(), is("Partly sunny"));
      assertThat(day.getNightSummary(), is("Clear"));
    }
  }
}
