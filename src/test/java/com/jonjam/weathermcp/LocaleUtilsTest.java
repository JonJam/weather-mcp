package com.jonjam.weathermcp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springaicommunity.mcp.annotation.McpMeta;

@DisplayName("LocaleUtils")
class LocaleUtilsTest {

  @Nested
  @DisplayName("resolveLocale")
  class ResolveLocale {

    @Test
    @DisplayName("returns default locale when no locale provided in meta")
    void noLocaleInMeta_returnsDefaultLocale() {
      // Arrange
      final Map<String, Object> metadata = new HashMap<>();
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final Locale result = LocaleUtils.resolveLocale(meta);

      // Assert
      assertThat(result, is(equalTo(Locale.US)));
    }

    @Test
    @DisplayName("returns default locale when locale in meta is null")
    void nullLocaleInMeta_returnsDefaultLocale() {
      // Arrange
      final Map<String, Object> metadata = new HashMap<>();
      metadata.put("locale", null);
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final Locale result = LocaleUtils.resolveLocale(meta);

      // Assert
      assertThat(result, is(equalTo(Locale.US)));
    }

    @Test
    @DisplayName("returns default locale when locale in meta is blank")
    void blankLocaleInMeta_returnsDefaultLocale() {
      // Arrange
      final Map<String, Object> metadata = new HashMap<>();
      metadata.put("locale", "   ");
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final Locale result = LocaleUtils.resolveLocale(meta);

      // Assert
      assertThat(result, is(equalTo(Locale.US)));
    }

    @Test
    @DisplayName("returns default locale when locale in meta is invalid")
    void invalidLocaleInMeta_returnsDefaultLocale() {
      // Arrange – tag that parses to a locale with empty language
      final Map<String, Object> metadata = new HashMap<>();
      metadata.put("locale", "test");
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final Locale result = LocaleUtils.resolveLocale(meta);

      // Assert
      assertThat(result, is(equalTo(Locale.US)));
    }

    @Test
    @DisplayName("returns locale when valid locale provided in meta")
    void validLocaleInMeta_returnsLocale() {
      // Arrange
      final Map<String, Object> metadata = new HashMap<>();
      metadata.put("locale", "en-GB");
      final McpMeta meta = new McpMeta(metadata);

      // Act
      final Locale result = LocaleUtils.resolveLocale(meta);

      // Assert
      assertThat(result, is(equalTo(Locale.UK)));
    }
  }
}
