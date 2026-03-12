package com.jonjam.weathermcp.currentconditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import java.util.HashMap;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springaicommunity.mcp.annotation.McpMeta;

@DisplayName("CurrentConditionsProvider")
class CurrentConditionsProviderTest {

  private final CurrentConditionsProvider provider = new CurrentConditionsProvider();

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
      final CallToolResult result = provider.currentConditionsTool("", meta);

      // Assert
      assertThat(result.isError(), is(true));
      assertThat(result.content(), is(not(nullValue())));
      assertThat(result.content().isEmpty(), is(false));
    }

    @Test
    @DisplayName("returns error when location is only whitespace")
    void returnsErrorWhenLocationIsOnlyWhitespace() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("   ", meta);

      // Assert
      assertThat(result.isError(), is(true));
      assertThat(result.content(), is(not(nullValue())));
      assertThat(result.content().isEmpty(), is(false));
    }

    @Test
    @DisplayName("returns error when location is shorter than minimum length")
    void returnsErrorWhenLocationIsShorterThanMinimumLength() {
      // Arrange
      final var metadata = new HashMap<String, Object>();
      metadata.put("locale", Locale.US.toLanguageTag());
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final CallToolResult result = provider.currentConditionsTool("ab", meta);

      // Assert
      assertThat(result.isError(), is(true));
      assertThat(result.content(), is(not(nullValue())));
      assertThat(result.content().isEmpty(), is(false));
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
      final CallToolResult result = provider.currentConditionsTool(overMaxLength, meta);

      // Assert
      assertThat(result.isError(), is(true));
      assertThat(result.content(), is(not(nullValue())));
      assertThat(result.content().isEmpty(), is(false));
    }
  }
}
