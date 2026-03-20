package com.jonjam.accuweathermcp.dailyforecast;

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
@DisplayName("DailyForecastProvider")
class DailyForecastProviderTest {

  @Mock private LocationsTextSearchGateway locationsTextSearchGateway;

  @Mock private DailyForecastGateway dailyForecastGateway;

  @Mock private DailyForecastToolResultMapper dailyForecastToolResultMapper;

  @Mock private McpSyncRequestContext context;

  @InjectMocks private DailyForecastProvider provider;

  @Nested
  @DisplayName("dailyForecastPrompt")
  class DailyForecastPrompt {

    @Test
    @DisplayName("includes location.")
    void includesLocation() {
      // Arrange

      // Act
      final String prompt = provider.dailyForecastPrompt("Valencia");

      // Assert
      assertThat(prompt, is("Provide the daily weather forecast for Valencia."));
    }
  }

  @Nested
  @DisplayName("dailyForecastTool")
  class DailyForecastTool {

    @Test
    @DisplayName("returns error when location is empty")
    void returnsErrorWhenLocationIsEmpty() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.dailyForecastTool("", context, meta);

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
      final CallToolResult result = provider.dailyForecastTool("   ", context, meta);

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
      final CallToolResult result = provider.dailyForecastTool("ab", context, meta);

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
      final CallToolResult result = provider.dailyForecastTool(overMaxLength, context, meta);

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
      final CallToolResult result = provider.dailyForecastTool("Nowhere", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(content.text(), containsString("No locations were found matching 'Nowhere'."));
    }

    @Test
    @DisplayName("returns error when daily forecast is unavailable")
    void returnsErrorWhenDailyForecastIsUnavailable() {
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

      when(dailyForecastGateway.getFiveDayForecast("12345", Locale.US))
          .thenReturn(Optional.empty());

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.dailyForecastTool("Somewhere", context, meta);

      // Assert
      assertThat(result.isError(), is(true));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(
          content.text(), containsString("Daily forecast is not available for 'Somewhere'."));
    }

    @Test
    @DisplayName("returns success when daily forecast is retrieved")
    void returnsSuccessWhenDailyForecastIsRetrieved() {
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

      final DailyForecastSummaryDto summary =
          DailyForecastSummaryDto.builder()
              .headlineText("Nice weather")
              .headlineLink("https://example.com/daily")
              .days(
                  List.of(
                      DailyForecastDaySummaryDto.builder()
                          .date("2026-01-01T07:00:00-08:00")
                          .minimumTemperature(10.0f)
                          .maximumTemperature(20.0f)
                          .temperatureUnit("C")
                          .daySummary("Sunny")
                          .nightSummary("Clear")
                          .build()))
              .build();

      final DailyForecastToolResult toolResult =
          DailyForecastToolResult.builder()
              .locationLocalizedName("San Francisco")
              .countryLocalizedName("United States")
              .headlineText(summary.getHeadlineText())
              .headlineLink(summary.getHeadlineLink())
              .days(
                  List.of(
                      DailyForecastDayToolResult.builder()
                          .date("2026-01-01T07:00:00-08:00")
                          .minimumTemperature(10.0f)
                          .maximumTemperature(20.0f)
                          .temperatureUnit("C")
                          .daySummary("Sunny")
                          .nightSummary("Clear")
                          .build()))
              .build();

      when(dailyForecastGateway.getFiveDayForecast("98765", Locale.US))
          .thenReturn(Optional.of(summary));

      when(dailyForecastToolResultMapper.toToolResult("San Francisco", "United States", summary))
          .thenReturn(toolResult);

      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.dailyForecastTool("San Francisco", context, meta);

      // Assert
      assertThat(result.isError(), is(false));
      assertThat(result.structuredContent(), is(toolResult));

      final TextContent content = (TextContent) result.content().getFirst();
      assertThat(content.text(), containsString("Location: San Francisco, Country: United States"));
      assertThat(content.text(), containsString("Headline: Nice weather"));
      assertThat(content.text(), containsString("2026-01-01T07:00:00-08:00"));
      assertThat(content.text(), containsString("Low 10.0°C"));
      assertThat(content.text(), containsString("High 20.0°C"));
      assertThat(content.text(), containsString("Day: Sunny"));
      assertThat(content.text(), containsString("Night: Clear"));
      assertThat(content.text(), containsString("More detail: https://example.com/daily"));
    }
  }
}
