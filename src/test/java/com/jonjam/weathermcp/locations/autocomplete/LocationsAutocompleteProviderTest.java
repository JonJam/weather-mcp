package com.jonjam.weathermcp.locations.autocomplete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocationsAutocompleteProvider")
class LocationsAutocompleteProviderTest {

  @Mock private LocationsAutocompleteGateway gateway;

  @InjectMocks private LocationsAutocompleteProvider provider;

  @Nested
  @DisplayName("autocompleteForCitiesAndPointsOfInterest")
  class AutocompleteForCitiesAndPointsOfInterest {

    @Test
    @DisplayName("completes location using gateway")
    void completesLocationUsingGateway() {
      // Arrange
      when(gateway.autocompleteForCitiesAndPointsOfInterest("san", Locale.US))
          .thenReturn(
              List.of(
                  LocationSuggestionDto.builder()
                      .id("12345")
                      .localizedName("San Francisco")
                      .countryKey("US")
                      .countryLocalizedName("United States")
                      .build()));

      // Act
      final CompleteResult result = provider.completeLocation("san", Locale.US.toLanguageTag());

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(List.of("San Francisco, United States")));
    }
  }
}
