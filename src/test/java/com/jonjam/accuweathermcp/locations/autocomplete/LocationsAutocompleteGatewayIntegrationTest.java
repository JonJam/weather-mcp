package com.jonjam.accuweathermcp.locations.autocomplete;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.jonjam.accuweathermcp.locations.common.LocationSuggestionDto;
import java.util.List;
import java.util.Locale;
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
class LocationsAutocompleteGatewayIntegrationTest {

  @InjectWireMock private WireMockServer wireMock;

  @Autowired private LocationsAutocompleteGateway gateway;

  @Nested
  @DisplayName("autocompleteForCitiesAndPointsOfInterest")
  class AutocompleteForCitiesAndPointsOfInterest {

    @Test
    @DisplayName("calls AccuWeather over HttpServiceClient")
    void callsAccuWeatherOverHttpServiceClient() {
      // Arrange
      wireMock.stubFor(
          get(urlPathEqualTo("/locations/v1/autocomplete"))
              .willReturn(
                  aResponse()
                      .withStatus(200)
                      .withHeader("Content-Type", "application/json")
                      .withBodyFile("locations-autocomplete-san.json")));

      final Locale locale = Locale.forLanguageTag("en-us");

      // Act
      final List<LocationSuggestionDto> result =
          gateway.autocompleteForCitiesAndPointsOfInterest("san", locale);

      // Assert
      final List<String> localizedNames =
          result.stream().map(LocationSuggestionDto::getLocalizedName).toList();

      assertThat(localizedNames.size(), is(10));
      assertThat(
          localizedNames,
          hasItems(
              "San Francisco Coacalco",
              "San Francisco",
              "San Francisco De Macoris",
              "San Francisco Solano",
              "San Francisco de Campeche",
              "San Francisco del Rincón"));
    }
  }
}
