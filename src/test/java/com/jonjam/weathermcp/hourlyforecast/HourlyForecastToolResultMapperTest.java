package com.jonjam.weathermcp.hourlyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
              .detailLink("https://example.com/hourly")
              .hours(
                  List.of(
                      HourlyForecastHourSummaryDto.builder()
                          .dateTime("2026-03-19T14:00:00+00:00")
                          .iconPhrase("Intermittent clouds")
                          .temperatureValue(18.5f)
                          .temperatureUnit("C")
                          .build()))
              .build();

      // Act
      final HourlyForecastToolResult result = mapper.toToolResult("Valencia", "Spain", summary);

      // Assert
      assertThat(result.getLocationLocalizedName(), is("Valencia"));
      assertThat(result.getCountryLocalizedName(), is("Spain"));
      assertThat(result.getDetailLink(), is("https://example.com/hourly"));
      assertThat(result.getHours(), hasSize(1));

      final HourlyForecastHourToolResult hour = result.getHours().get(0);
      assertThat(hour.getDateTime(), is("2026-03-19T14:00:00+00:00"));
      assertThat(hour.getIconPhrase(), is("Intermittent clouds"));
      assertThat(hour.getTemperatureValue(), is(18.5f));
      assertThat(hour.getTemperatureUnit(), is("C"));
    }

    @Test
    @DisplayName("maps summary with null detailLink to tool result")
    void mapsSummaryWithNullDetailLinkToToolResult() {
      // Arrange
      final HourlyForecastSummaryDto summary =
          HourlyForecastSummaryDto.builder()
              .detailLink(null)
              .hours(
                  List.of(
                      HourlyForecastHourSummaryDto.builder()
                          .dateTime("2026-03-19T15:00:00+00:00")
                          .iconPhrase("Partly sunny")
                          .temperatureValue(19.2f)
                          .temperatureUnit("C")
                          .build()))
              .build();

      // Act
      final HourlyForecastToolResult result = mapper.toToolResult("Lima", "Peru", summary);

      // Assert
      assertThat(result.getDetailLink(), is(nullValue()));
      assertThat(result.getHours(), hasSize(1));
    }
  }
}
