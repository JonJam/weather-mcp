package com.jonjam.weathermcp.dailyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DailyForecastToolResultMapper")
class DailyForecastToolResultMapperTest {

  private final DailyForecastToolResultMapper mapper = new DailyForecastToolResultMapper();

  @Nested
  @DisplayName("toToolResult")
  class ToToolResult {

    @Test
    @DisplayName("maps summary and location names to tool result")
    void mapsSummaryAndLocationNamesToToolResult() {
      // Arrange
      final DailyForecastSummaryDto summary =
          DailyForecastSummaryDto.builder()
              .headlineText("Mild week ahead")
              .headlineLink("https://example.com/forecast")
              .days(
                  List.of(
                      DailyForecastDaySummaryDto.builder()
                          .date("2026-01-02T07:00:00+00:00")
                          .minimumTemperature(3.5f)
                          .maximumTemperature(9.0f)
                          .temperatureUnit("C")
                          .daySummary("Partly sunny")
                          .nightSummary("Clear")
                          .build()))
              .build();

      // Act
      final DailyForecastToolResult result =
          mapper.toToolResult("Springfield", "United States", summary);

      // Assert
      assertThat(result.getLocationLocalizedName(), is("Springfield"));
      assertThat(result.getCountryLocalizedName(), is("United States"));
      assertThat(result.getHeadlineText(), is("Mild week ahead"));
      assertThat(result.getHeadlineLink(), is("https://example.com/forecast"));
      assertThat(result.getDays(), hasSize(1));

      final DailyForecastDayToolResult day = result.getDays().get(0);
      assertThat(day.getDate(), is("2026-01-02T07:00:00+00:00"));
      assertThat(day.getMinimumTemperature(), is(3.5f));
      assertThat(day.getMaximumTemperature(), is(9.0f));
      assertThat(day.getTemperatureUnit(), is("C"));
      assertThat(day.getDaySummary(), is("Partly sunny"));
      assertThat(day.getNightSummary(), is("Clear"));
    }
  }
}
