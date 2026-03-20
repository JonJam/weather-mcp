package com.jonjam.weathermcp.currentconditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.jonjam.weathermcp.locations.common.LocationSuggestionDto;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springaicommunity.mcp.context.McpSyncRequestContext;

@ExtendWith(MockitoExtension.class)
@DisplayName("CurrentConditionsProvider")
class CurrentConditionsProviderTest {

  @Mock private CurrentConditionsGateway currentConditionsGateway;

  @Mock
  private com.jonjam.weathermcp.locations.textsearch.LocationsTextSearchGateway
      locationsTextSearchGateway;

  @Mock private CurrentConditionsToolResultMapper currentConditionsToolResultMapper;

  @Mock private McpSyncRequestContext context;

  @InjectMocks private CurrentConditionsProvider provider;

  @Nested
  @DisplayName("currentConditionsTool")
  class CurrentConditionsTool {

    @Test
    @DisplayName("returns error when location is empty")
    void returnsErrorWhenLocationIsEmpty() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(),
          containsString(
              "The 'location' parameter must be between 3 and 100 characters of non-blank text."));
    }

    @Test
    @DisplayName("returns error when location is only whitespace")
    void returnsErrorWhenLocationIsOnlyWhitespace() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("   ", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(),
          containsString(
              "The 'location' parameter must be between 3 and 100 characters of non-blank text."));
    }

    @Test
    @DisplayName("returns error when location is shorter than minimum length")
    void returnsErrorWhenLocationIsShorterThanMinimumLength() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("ab", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(),
          containsString(
              "The 'location' parameter must be between 3 and 100 characters of non-blank text."));
    }

    @Test
    @DisplayName("returns error when location is longer than maximum length")
    void returnsErrorWhenLocationIsLongerThanMaximumLength() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      final String overMaxLength = "a".repeat(101);

      // Act
      final CallToolResult result = provider.currentConditionsTool(overMaxLength, context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(),
          containsString(
              "The 'location' parameter must be between 3 and 100 characters of non-blank text."));
    }

    @Test
    @DisplayName("returns error when no location is found")
    void returnsErrorWhenNoLocationIsFound() {
      // Arrange
      when(locationsTextSearchGateway.search("Nowhere", Locale.US)).thenReturn(Optional.empty());

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("Nowhere", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(content.text(), containsString("No locations were found matching 'Nowhere'."));
    }

    @Test
    @DisplayName("returns error when current conditions are unavailable")
    void returnsErrorWhenCurrentConditionsAreUnavailable() {
      // Arrange
      when(locationsTextSearchGateway.search("Somewhere", Locale.US))
          .thenReturn(
              Optional.of(
                  LocationSuggestionDto.builder()
                      .id("12345")
                      .localizedName("Somewhere")
                      .countryKey("US")
                      .countryLocalizedName("United States")
                      .build()));

      when(currentConditionsGateway.getCurrentConditions("12345", Locale.US))
          .thenReturn(Optional.empty());

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("Somewhere", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(), containsString("Current conditions are not available for 'Somewhere'."));
    }

    @Test
    @DisplayName("returns success when current conditions are retrieved")
    void returnsSuccessWhenCurrentConditionsAreRetrieved() {
      // Arrange
      when(locationsTextSearchGateway.search("San Francisco", Locale.US))
          .thenReturn(
              Optional.of(
                  LocationSuggestionDto.builder()
                      .id("98765")
                      .localizedName("San Francisco")
                      .countryKey("US")
                      .countryLocalizedName("United States")
                      .build()));

      final CurrentConditionsDto currentConditions =
          CurrentConditionsDto.builder()
              .localObservationDateTime("2024-01-01T12:00:00-08:00")
              .weatherText("Sunny")
              .temperatureMetric(20L)
              .temperatureImperial(68L)
              .link("https://example.com/current-conditions")
              .build();

      final CurrentConditionsToolResult toolResult =
          CurrentConditionsToolResult.builder()
              .locationLocalizedName("San Francisco")
              .countryLocalizedName("United States")
              .localObservationDateTime(currentConditions.getLocalObservationDateTime())
              .weatherText(currentConditions.getWeatherText())
              .temperatureMetric(currentConditions.getTemperatureMetric())
              .temperatureImperial(currentConditions.getTemperatureImperial())
              .link(currentConditions.getLink())
              .build();

      when(currentConditionsGateway.getCurrentConditions("98765", Locale.US))
          .thenReturn(Optional.of(currentConditions));

      when(currentConditionsToolResultMapper.toToolResult(
              "San Francisco", "United States", currentConditions))
          .thenReturn(toolResult);

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("San Francisco", context, meta);

      // Assert
      assertThat(result.isError(), is(false));
      assertThat(result.structuredContent(), is(toolResult));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(),
          is(
              "Location: San Francisco, Country: United States, Temperature: 20.0°C (68.0°F), Conditions: Sunny"));
    }
  }

  @Nested
  @DisplayName("currentConditionsPrompt")
  class CurrentConditionsPrompt {

    @Test
    @DisplayName("includes location")
    void includesLocation() {
      // Arrange

      // Act
      final String prompt = provider.currentConditionsPrompt("London");

      // Assert
      assertThat(prompt, is(equalTo("Provide the current weather conditions for London.")));
    }
  }
}
