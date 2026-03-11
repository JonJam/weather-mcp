# Agents and Project Guidance

This file defines how agents should work in this repository and where to find reusable skills.

- **Rules file**: Use this document to describe high-level behavior for agents working on this project.
- **Skills directory**: Custom skills live in the `.agents/skills/` directory.

## Rules

- **Purpose**: Describe project-specific expectations, conventions, and constraints for agents.
- **Examples**:
  - Coding style, frameworks, and libraries to prefer.
  - Files or areas of the codebase that need extra care.
  - Any constraints (e.g. no external network calls, no schema changes without approval).

Add rules below this line:

### Spring Boot

- **Dependency versions**: When using Spring Boot with the `io.spring.dependency-management` plugin, do not specify versions on individual dependencies in the `dependencies {}` block. Rely on BOMs (for example, `spring-boot-dependencies` and technology-specific BOMs like `spring-ai-bom`) to control versions centrally, as recommended in the Spring Boot dependency management documentation.

### Null handling

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

### DTOs

- **Immutable DTOs**: DTOs are modeled as immutable value types using Lombok's `@Value`, `@Builder` and `@AllArgsConstructor(access = AccessLevel.PRIVATE)`
- Typical pattern:
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

### Tests
- **Use hamcrest assertions**

---

### Skills

- **Location**: `.agents/skills/`
- **Usage**: Each skill should be documented in its own file, explaining:
  - What the skill does.
  - When agents should use it.
  - Any configuration or assumptions.

You can start by adding markdown or JSON/YAML skill descriptions under `.agents/skills/`.