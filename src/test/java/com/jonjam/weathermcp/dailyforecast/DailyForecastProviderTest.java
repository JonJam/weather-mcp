package com.jonjam.weathermcp.dailyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DailyForecastProvider")
class DailyForecastProviderTest {

  private final DailyForecastProvider provider = new DailyForecastProvider();

  @Nested
  @DisplayName("dailyForecastPrompt")
  class DailyForecastPrompt {

    @Test
    @DisplayName("includes location.")
    void includesLocation() {
      // Arrange

      // Act
      final String prompt = provider.dailyForecastPrompt("Valencia");

      // Assert
      assertThat(prompt, is("Provide the daily weather forecast for Valencia."));
    }
  }
}
