package com.jonjam.weathermcp.autocomplete;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.wiremock.integrations.springboot.EnableWireMock;
import org.wiremock.integrations.springboot.ConfigureWireMock;

@SpringBootTest
@EnableWireMock(
    @ConfigureWireMock(baseUrlProperties = "app.accuweather.base-url"))
@TestPropertySource(properties = "app.accuweather.api-key=test-key")
class AutocompleteGatewayIntegrationTest {

  @Autowired AutocompleteGateway gateway;

  @BeforeEach
  void stubAccuWeatherAutocomplete() {
    stubFor(
        get(urlPathEqualTo("/locations/v1/autocomplete"))
            .withQueryParam("q", WireMock.equalTo("san"))
            .withQueryParam("language", WireMock.equalTo("en-us"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                        [
                          {"Key": "1", "LocalizedName": "San Francisco", "Type": "City"}
                        ]
                        """)));
  }

  @Test
  void gatewayCallsAccuWeatherOverHttpServiceClient() {
    List<String> result = gateway.autocompleteForCitiesAndPointsOfInterest("san", "en-us");

    assertThat(result).containsExactly("San Francisco");
  }
}
