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
class LocationsAutocompleteProviderTest {

  @Mock AutocompleteGateway gateway;

  @InjectMocks LocationsAutocompleteProvider provider;

  @Test
  void completesLocationUsingGateway() {
    when(gateway.autocompleteForCitiesAndPointsOfInterest("san", null))
        .thenReturn(List.of("San Francisco"));

    var result = provider.completeLocation("san");

    assertThat(result).containsExactly("San Francisco");
  }
}

