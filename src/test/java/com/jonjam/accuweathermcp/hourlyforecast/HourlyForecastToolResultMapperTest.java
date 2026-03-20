package com.jonjam.accuweathermcp.hourlyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HourlyForecastToolResultMapper")
class HourlyForecastToolResultMapperTest {

  private final HourlyForecastToolResultMapper mapper = new HourlyForecastToolResultMapper();

  @Nested
  @DisplayName("toToolResult")
  class ToToolResult {

    @Test
    @DisplayName("maps summary and location names to tool result")
    void mapsSummaryAndLocationNamesToToolResult() {
      // Arrange
      final HourlyForecastSummaryDto summary =
          HourlyForecastSummaryDto.builder()
              .hours(
                  List.of(
                      HourlyForecastHourSummaryDto.builder()
                          .dateTime("2026-03-19T14:00:00+00:00")
                          .iconPhrase("Intermittent clouds")
                          .temperatureValue(18.5f)
                          .temperatureUnit("C")
                          .link("https://example.com/hourly")
                          .build()))
              .build();

      // Act
      final HourlyForecastToolResult result = mapper.toToolResult("Valencia", "Spain", summary);

      // Assert
      assertThat(result.getLocationLocalizedName(), is("Valencia"));
      assertThat(result.getCountryLocalizedName(), is("Spain"));
      assertThat(result.getHours(), hasSize(1));

      final HourlyForecastHourToolResult hour = result.getHours().get(0);
      assertThat(hour.getDateTime(), is("2026-03-19T14:00:00+00:00"));
      assertThat(hour.getIconPhrase(), is("Intermittent clouds"));
      assertThat(hour.getTemperatureValue(), is(18.5f));
      assertThat(hour.getTemperatureUnit(), is("C"));
      assertThat(hour.getLink(), is("https://example.com/hourly"));
    }
  }
}
