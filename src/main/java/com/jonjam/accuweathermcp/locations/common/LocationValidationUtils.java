package com.jonjam.accuweathermcp.locations.common;

import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;

public final class LocationValidationUtils {

  static final int MIN_LOCATION_LENGTH = 3;
  static final int MAX_LOCATION_LENGTH = 100;

  private LocationValidationUtils() {}

  public static Optional<String> normalizeAndValidateLocation(final @Nullable String rawLocation) {

    if (rawLocation == null) {
      return Optional.empty();
    }

    final String trimmedLocation = rawLocation.trim();

    if (!StringUtils.hasText(trimmedLocation)
        || trimmedLocation.length() < MIN_LOCATION_LENGTH
        || trimmedLocation.length() > MAX_LOCATION_LENGTH) {
      return Optional.empty();
    }

    return Optional.of(trimmedLocation);
  }
}
