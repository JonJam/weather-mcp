package com.jonjam.weathermcp.locations.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LocationValidationUtils")
class LocationValidationUtilsTest {

  @Nested
  @DisplayName("normalizeAndValidateLocation")
  class NormalizeAndValidateLocation {

    @Test
    @DisplayName("returns empty Optional when location is null")
    void returnsEmptyOptionalWhenLocationIsNull() {
      // Arrange

      // Act
      final Optional<String> result = LocationValidationUtils.normalizeAndValidateLocation(null);

      // Assert
      assertThat(result.isEmpty(), is(true));
    }

    @Test
    @DisplayName("returns empty Optional when location is empty")
    void returnsEmptyOptionalWhenLocationIsEmpty() {
      // Arrange

      // Act
      final Optional<String> result = LocationValidationUtils.normalizeAndValidateLocation("");

      // Assert
      assertThat(result.isEmpty(), is(true));
    }

    @Test
    @DisplayName("returns empty Optional when location is only whitespace")
    void returnsEmptyOptionalWhenLocationIsOnlyWhitespace() {
      // Arrange

      // Act
      final Optional<String> result = LocationValidationUtils.normalizeAndValidateLocation("   ");

      // Assert
      assertThat(result.isEmpty(), is(true));
    }

    @Test
    @DisplayName("returns empty Optional when location is shorter than minimum length")
    void returnsEmptyOptionalWhenLocationIsShorterThanMinimumLength() {
      // Arrange

      // Act
      final Optional<String> result = LocationValidationUtils.normalizeAndValidateLocation("ab");

      // Assert
      assertThat(result.isEmpty(), is(true));
    }

    @Test
    @DisplayName("returns empty Optional when location is longer than maximum length")
    void returnsEmptyOptionalWhenLocationIsLongerThanMaximumLength() {
      // Arrange
      final String overMaxLength = "a".repeat(LocationValidationUtils.MAX_LOCATION_LENGTH + 1);

      // Act
      final Optional<String> result =
          LocationValidationUtils.normalizeAndValidateLocation(overMaxLength);

      // Assert
      assertThat(result.isEmpty(), is(true));
    }

    @Test
    @DisplayName("returns trimmed value when location is valid")
    void returnsTrimmedValueWhenLocationIsValid() {
      // Arrange

      // Act
      final Optional<String> result =
          LocationValidationUtils.normalizeAndValidateLocation("  Manchester  ");

      // Assert
      assertThat(result.isEmpty(), is(false));
      assertThat(result.orElseThrow(), is("Manchester"));
    }

    @Test
    @DisplayName("accepts value at minimum and maximum length")
    void acceptsValueAtMinimumAndMaximumLength() {
      // Arrange
      final String minLength = "a".repeat(LocationValidationUtils.MIN_LOCATION_LENGTH);
      final String maxLength = "a".repeat(LocationValidationUtils.MAX_LOCATION_LENGTH);

      // Act
      final Optional<String> minResult =
          LocationValidationUtils.normalizeAndValidateLocation(minLength);
      final Optional<String> maxResult =
          LocationValidationUtils.normalizeAndValidateLocation(maxLength);

      // Assert
      assertThat(minResult.isEmpty(), is(false));
      assertThat(minResult.orElseThrow(), is(minLength));
      assertThat(maxResult.isEmpty(), is(false));
      assertThat(maxResult.orElseThrow(), is(maxLength));
    }
  }
}
