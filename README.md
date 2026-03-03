# weather-mcp

## Code style

This project uses the Spotless Gradle plugin to enforce a consistent Java style.

Common commands:

- `./gradlew spotlessApply` — format sources and fix style issues.
- `./gradlew spotlessCheck` — verify formatting without changing files.

## Null handling

- **Package defaults**: Each Java package in this project should declare a `package-info.java` file that applies `@NullMarked`:

  ```java
  @NullMarked
  package com.jonjam.weathermcp;

  import org.jspecify.annotations.NullMarked;
  ```

- **Nullable values**: Anywhere a value can legitimately be `null` (fields, parameters, return types, type arguments), it **must** be explicitly annotated with `@Nullable` from JSpecify:

  ```java
  import org.jspecify.annotations.Nullable;

  public String greet(@Nullable String name) { ... }
  ```

- **Non-null by default**: Under `@NullMarked`, all unannotated types are treated as non-null, so `@Nullable` is the only marker needed to indicate possible absence.

## DTOs

- **Immutable DTOs**: DTOs are modeled as immutable value types using Lombok's `@Value`, `@Builder` and `@AllArgsConstructor(access = AccessLevel.PRIVATE)`
- **Typical pattern**:

  ```java
    import lombok.AccessLevel;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Value;

    @Value
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class WeatherSummaryDto {
      String location;
      double temperatureCelsius;
      String condition;
    }
  ```

  Lombok generates a final class with private final fields, getters, and sensible `equals`, `hashCode`, and `toString` implementations. The constructor is private so callers must use `WeatherSummaryDto.builder()...build()`.

## Logging

- **Accessing a logger**: Use Lombok's `@CommonsLog` on classes that need logging:

  ```java
  import lombok.extern.apachecommons.CommonsLog;

  @CommonsLog
  public class ExampleService {

    public void doSomething() {
      log.info("Starting work");
    }
  }
  ```

  Lombok generates a `private static final org.apache.commons.logging.Log log` field so you can log via `log.info(...)`, `log.warn(...)`, etc.
