package com.jonjam.accuweathermcp.dailyforecast;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@ActiveProfiles("test")
@SpringBootTest
@EnableWireMock(
    @ConfigureWireMock(baseUrlProperties = "spring.http.serviceclient.accuweather.base-url"))
class DailyForecastGatewayIntegrationTest {

  @InjectWireMock private WireMockServer wireMock;

  @Autowired private DailyForecastGateway gateway;

  @Nested
  @DisplayName("getFiveDayForecast")
  class GetFiveDayForecast {

    @Test
    @DisplayName("returns flattened summary when AccuWeather returns daily forecasts")
    void returnsFlattenedSummaryWhenAccuWeatherReturnsDailyForecasts() {
      // Arrange
      wireMock.stubFor(
          get(urlPathEqualTo("/forecasts/v1/daily/5day/352579"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("daily-forecast-five-day-valencia-spain.json")));

      final Locale locale = Locale.forLanguageTag("en-us");

      // Act
      final Optional<DailyForecastSummaryDto> result = gateway.getFiveDayForecast("352579", locale);

      // Assert
      final DailyForecastSummaryDto summary = result.orElseThrow();

      assertThat(summary.getHeadlineText(), is("Mostly sunny this weekend"));
      assertThat(
          summary.getHeadlineLink(),
          is(
              "http://www.accuweather.com/en/ve/valencia/352579/daily-weather-forecast/352579?unit=c&lang=en-us"));
      assertThat(summary.getDays(), hasSize(5));

      final DailyForecastDaySummaryDto firstDay = summary.getDays().get(0);
      assertThat(firstDay.getDate(), is("2026-03-18T07:00:00-04:00"));
      assertThat(firstDay.getMinimumTemperature(), is(19.9f));
      assertThat(firstDay.getMaximumTemperature(), is(32.9f));
      assertThat(firstDay.getTemperatureUnit(), is("C"));
      assertThat(firstDay.getDaySummary(), is("Intermittent clouds"));
      assertThat(firstDay.getNightSummary(), is("Intermittent clouds"));
    }
  }
}
