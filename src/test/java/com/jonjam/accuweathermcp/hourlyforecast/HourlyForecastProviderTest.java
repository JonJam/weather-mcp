package com.jonjam.accuweathermcp.hourlyforecast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.jonjam.accuweathermcp.locations.common.LocationSuggestionDto;
import com.jonjam.accuweathermcp.locations.textsearch.LocationsTextSearchGateway;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.util.HashMap;
import java.util.List;
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
@DisplayName("HourlyForecastProvider")
class HourlyForecastProviderTest {

  @Mock private LocationsTextSearchGateway locationsTextSearchGateway;

  @Mock private HourlyForecastGateway hourlyForecastGateway;

  @Mock private HourlyForecastToolResultMapper hourlyForecastToolResultMapper;

  @Mock private McpSyncRequestContext context;

  @InjectMocks private HourlyForecastProvider provider;

  @Nested
  @DisplayName("hourlyForecastPrompt")
  class HourlyForecastPrompt {

    @Test
    @DisplayName("includes location.")
    void includesLocation() {
      // Arrange

      // Act
      final String prompt = provider.hourlyForecastPrompt("Valencia");

      // Assert
      assertThat(prompt, is("Provide the hourly weather forecast for Valencia."));
    }
  }

  @Nested
  @DisplayName("hourlyForecastTool")
  class HourlyForecastTool {

    @Test
    @DisplayName("returns error when location is empty")
    void returnsErrorWhenLocationIsEmpty() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.hourlyForecastTool("", context, meta);

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
      final CallToolResult result = provider.hourlyForecastTool("Nowhere", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(content.text(), containsString("No locations were found matching 'Nowhere'."));
    }

    @Test
    @DisplayName("returns error when hourly forecast is unavailable")
    void returnsErrorWhenHourlyForecastIsUnavailable() {
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

      when(hourlyForecastGateway.getHourlyForecastForTwelveHours("12345", Locale.US))
          .thenReturn(Optional.empty());

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.hourlyForecastTool("Somewhere", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(), containsString("Hourly forecast is not available for 'Somewhere'."));
    }

    @Test
    @DisplayName("returns success when hourly forecast is retrieved")
    void returnsSuccessWhenHourlyForecastIsRetrieved() {
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

      final HourlyForecastSummaryDto summary =
          HourlyForecastSummaryDto.builder()
              .hours(
                  List.of(
                      HourlyForecastHourSummaryDto.builder()
                          .dateTime("2026-03-19T14:00:00-08:00")
                          .iconPhrase("Partly cloudy")
                          .temperatureValue(18.5f)
                          .temperatureUnit("C")
                          .link("https://example.com/hourly")
                          .build()))
              .build();

      final HourlyForecastToolResult toolResult =
          HourlyForecastToolResult.builder()
              .locationLocalizedName("San Francisco")
              .countryLocalizedName("United States")
              .hours(
                  List.of(
                      HourlyForecastHourToolResult.builder()
                          .dateTime("2026-03-19T14:00:00-08:00")
                          .iconPhrase("Partly cloudy")
                          .temperatureValue(18.5f)
                          .temperatureUnit("C")
                          .link("https://example.com/hourly")
                          .build()))
              .build();

      when(hourlyForecastGateway.getHourlyForecastForTwelveHours("98765", Locale.US))
          .thenReturn(Optional.of(summary));

      when(hourlyForecastToolResultMapper.toToolResult("San Francisco", "United States", summary))
          .thenReturn(toolResult);

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.hourlyForecastTool("San Francisco", context, meta);

      // Assert
      assertThat(result.isError(), is(false));
      assertThat(result.structuredContent(), is(toolResult));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(content.text(), containsString("Location: San Francisco, Country: United States"));
      assertThat(content.text(), containsString("2026-03-19T14:00:00-08:00"));
      assertThat(content.text(), containsString("18.5°C"));
      assertThat(content.text(), containsString("Partly cloudy"));
      assertThat(content.text(), containsString("More detail: https://example.com/hourly"));
    }
  }
}
