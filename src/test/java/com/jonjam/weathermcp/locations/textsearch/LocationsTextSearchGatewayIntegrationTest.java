package com.jonjam.weathermcp.locations.textsearch;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.jonjam.weathermcp.locations.common.LocationSuggestionDto;
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
class LocationsTextSearchGatewayIntegrationTest {

  @InjectWireMock private WireMockServer wireMock;

  @Autowired private LocationsTextSearchGateway gateway;

  @Nested
  @DisplayName("search")
  class Search {

    @Test
    @DisplayName("returns single location suggestion when AccuWeather returns results")
    void returnsSingleLocationSuggestionWhenAccuWeatherReturnsResults() {
      // Arrange
      wireMock.stubFor(
          get(urlPathEqualTo("/locations/v1/search"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("locations-search-lima-peru.json")));

      final Locale locale = Locale.forLanguageTag("en-us");

      // Act
      final Optional<LocationSuggestionDto> result = gateway.search("Lima, Peru", locale);

      // Assert
      assertThat(result.isPresent(), is(true));
      final LocationSuggestionDto suggestion = result.orElseThrow();
      assertThat(suggestion.getId(), is("264120"));
      assertThat(suggestion.getLocalizedName(), is("Lima"));
      assertThat(suggestion.getCountryKey(), is("PE"));
      assertThat(suggestion.getCountryLocalizedName(), is("Peru"));
    }

    @Test
    @DisplayName("returns empty when AccuWeather returns no results")
    void returnsEmptyWhenAccuWeatherReturnsNoResults() {
      // Arrange
      wireMock.stubFor(
          get(urlPathEqualTo("/locations/v1/search"))
              .withQueryParam("q", equalTo("NonexistentCityXYZ"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBody("[]")));
      final Locale locale = Locale.forLanguageTag("en-us");

      // Act
      final Optional<LocationSuggestionDto> result = gateway.search("NonexistentCityXYZ", locale);

      // Assert
      assertThat(result.isEmpty(), is(true));
    }
  }
}
