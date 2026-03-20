package com.jonjam.accuweathermcp.currentconditions;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
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
class CurrentConditionsGatewayIntegrationTest {

  @InjectWireMock private WireMockServer wireMock;

  @Autowired private CurrentConditionsGateway gateway;

  @Nested
  @DisplayName("getCurrentConditions")
  class GetCurrentConditions {

    @Test
    @DisplayName("returns current conditions when AccuWeather returns a result")
    void returnsCurrentConditionsWhenAccuWeatherReturnsAResult() {
      // Arrange
      wireMock.stubFor(
          get(urlPathEqualTo("/currentconditions/v1/329260"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("current-conditions-manchester-uk.json")));

      final Locale locale = Locale.forLanguageTag("en-us");

      // Act
      final Optional<CurrentConditionsDto> result = gateway.getCurrentConditions("329260", locale);

      // Assert
      assertThat(result.isPresent(), is(true));
      final CurrentConditionsDto currentConditions = result.orElseThrow();

      assertThat(currentConditions.getLocalObservationDateTime(), is("2026-03-12T17:47:00+00:00"));
      assertThat(currentConditions.getWeatherText(), is("Rain"));
      assertThat(currentConditions.getTemperatureImperial(), is(54f));
      assertThat(currentConditions.getTemperatureMetric(), is(12f));
      assertThat(
          currentConditions.getLink(),
          is(
              "https://www.accuweather.com/en/gb/manchester/m15-6/current-weather/329260?lang=en-us"));
    }
  }
}
