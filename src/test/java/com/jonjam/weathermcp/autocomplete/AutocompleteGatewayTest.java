package com.jonjam.weathermcp.autocomplete;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AutocompleteGatewayTest {

  @Mock AccuWeatherAutocompleteClient client;

  @InjectMocks AutocompleteGateway gateway;

  @Test
  void returnsNamesFromClientWithDefaultLanguageWhenMissing() {
    var dto1 = new AccuWeatherAutocompleteDto("1", "San Francisco", "City");
    var dto2 = new AccuWeatherAutocompleteDto("2", "San Jose", "City");

    when(client.autocompleteForCitiesAndPointsOfInterest("san", "en-us"))
        .thenReturn(List.of(dto1, dto2));

    var result = gateway.autocompleteForCitiesAndPointsOfInterest("san", null);

    assertThat(result).containsExactly("San Francisco", "San Jose");
  }

  @Test
  void returnsEmptyListWhenQueryBlank() {
    var result = gateway.autocompleteForCitiesAndPointsOfInterest("   ", "en-us");

    assertThat(result).isEmpty();
  }
}

