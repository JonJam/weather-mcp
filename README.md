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