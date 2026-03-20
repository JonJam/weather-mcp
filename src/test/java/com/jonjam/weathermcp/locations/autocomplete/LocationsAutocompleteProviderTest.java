package com.jonjam.weathermcp.locations.autocomplete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.jonjam.weathermcp.locations.common.LocationSuggestionDto;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springaicommunity.mcp.annotation.McpMeta;

@ExtendWith(MockitoExtension.class)
@DisplayName("LocationsAutocompleteProvider")
class LocationsAutocompleteProviderTest {

  @Mock private LocationsAutocompleteGateway gateway;

  @InjectMocks private LocationsAutocompleteProvider provider;

  @Nested
  @DisplayName("completeLocationForCurrentConditions")
  class CompleteLocationForCurrentConditions {

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

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CompleteResult result = provider.completeLocationForCurrentConditions("san", meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(List.of("San Francisco, United States")));
    }

    @Test
    @DisplayName("returns empty completion when partialLocation is null")
    void returnsEmptyCompletionWhenPartialLocationIsNull() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CompleteResult result = provider.completeLocationForCurrentConditions(null, meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(Collections.emptyList()));
    }

    @Test
    @DisplayName("returns empty completion when partialLocation is empty")
    void returnsEmptyCompletionWhenPartialLocationIsEmpty() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CompleteResult result = provider.completeLocationForCurrentConditions("", meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(Collections.emptyList()));
    }

    @Test
    @DisplayName("returns empty completion when partialLocation is shorter than minimum length")
    void returnsEmptyCompletionWhenPartialLocationIsShorterThanMinimumLength() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CompleteResult result = provider.completeLocationForCurrentConditions("ab", meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(Collections.emptyList()));
    }

    @Test
    @DisplayName("returns empty completion when partialLocation is longer than maximum length")
    void returnsEmptyCompletionWhenPartialLocationIsLongerThanMaximumLength() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      final String overMaxLength = "a".repeat(101);

      // Act
      final CompleteResult result =
          provider.completeLocationForCurrentConditions(overMaxLength, meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(Collections.emptyList()));
    }
  }

  @Nested
  @DisplayName("completeLocationForDailyForecast")
  class CompleteLocationForDailyForecast {

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

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CompleteResult result = provider.completeLocationForDailyForecast("san", meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(List.of("San Francisco, United States")));
    }
  }

  @Nested
  @DisplayName("completeLocationForHourlyForecast")
  class CompleteLocationForHourlyForecast {

    @Test
    @DisplayName("completes location using gateway")
    void completesLocationUsingGateway() {
      // Arrange
      when(gateway.autocompleteForCitiesAndPointsOfInterest("val", Locale.US))
          .thenReturn(
              List.of(
                  LocationSuggestionDto.builder()
                      .id("352579")
                      .localizedName("Valencia")
                      .countryKey("ES")
                      .countryLocalizedName("Spain")
                      .build()));

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CompleteResult result = provider.completeLocationForHourlyForecast("val", meta);

      // Assert
      final CompleteResult.CompleteCompletion completion = result.completion();

      assertThat(completion.hasMore(), is(false));
      assertThat(completion.values(), is(List.of("Valencia, Spain")));
    }
  }
}
