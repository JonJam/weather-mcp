package com.jonjam.weathermcp.hourlyforecast;

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
class HourlyForecastGatewayIntegrationTest {

  @InjectWireMock private WireMockServer wireMock;

  @Autowired private HourlyForecastGateway gateway;

  @Nested
  @DisplayName("getHourlyForecastForTwelveHours")
  class GetHourlyForecastForTwelveHours {

    @Test
    @DisplayName("returns summary when AccuWeather returns hourly forecasts")
    void returnsSummaryWhenAccuWeatherReturnsHourlyForecasts() {
      // Arrange
      wireMock.stubFor(
          get(urlPathEqualTo("/forecasts/v1/hourly/12hour/347936"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("hourly-forecast-12hour-miami-usa.json")));

      final Locale locale = Locale.forLanguageTag("en-us");

      // Act
      final Optional<HourlyForecastSummaryDto> result =
          gateway.getHourlyForecastForTwelveHours("347936", locale);

      // Assert
      final HourlyForecastSummaryDto summary = result.orElseThrow();

      assertThat(summary.getHours(), hasSize(12));

      final HourlyForecastHourSummaryDto firstHour = summary.getHours().get(0);
      assertThat(firstHour.getDateTime(), is("2026-03-20T04:00:00-04:00"));
      assertThat(firstHour.getIconPhrase(), is("Partly cloudy"));
      assertThat(firstHour.getTemperatureValue(), is(18.5f));
      assertThat(firstHour.getTemperatureUnit(), is("C"));
      assertThat(
          firstHour.getLink(),
          is(
              "http://www.accuweather.com/en/us/miami-fl/33128/hourly-weather-forecast/347936?day=1&hbhhour=4&unit=c&lang=en-us"));
    }
  }
}
