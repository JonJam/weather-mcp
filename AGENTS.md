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

### Java

- **Use final**: Prefer prefixing parameters and local variables with `final` where they are not reassigned, to make immutability explicit and avoid accidental reassignment.

### Spring Boot

- **Dependency versions**: When using Spring Boot with the `io.spring.dependency-management` plugin, do not specify versions on individual dependencies in the `dependencies {}` block. Rely on BOMs (for example, `spring-boot-dependencies` and technology-specific BOMs like `spring-ai-bom`) to control versions centrally, as recommended in the Spring Boot dependency management documentation.

### Build tooling

- **Gradle, not Maven**: This project uses Gradle as its build tool. Prefer Gradle commands (for example, `./gradlew test`) instead of Maven commands.

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
- **Use Hamcrest assertions**: Prefer Hamcrest for test assertions so failure messages and intent stay clear and consistent.
  - Use `org.hamcrest.MatcherAssert.assertThat(actual, matcher)` (static import: `assertThat` from `org.hamcrest.MatcherAssert`).
  - Use matchers from `org.hamcrest.Matchers` (e.g. `is()`, `equalTo()`, `not()`, `nullValue()`, `hasItems()`, `contains()`, `hasSize()`, `allOf()`, `anyOf()`) rather than raw equality or AssertJ in the same tests.
  - Keep a single assertion style per test class: use Hamcrest for all assertions in that class, and avoid mixing with AssertJ or JUnit `assertEquals`-style calls.
- **Test fields and variables**: Qualify test fields and locals with explicit scope (e.g. `private` for class-level mocks and the class under test).
- **Group tests by method**: Use JUnit 5 `@Nested` classes with `@DisplayName` to group test cases for a particular method; name the nested class after the method under test (e.g. `class AutocompleteForCitiesAndPointsOfInterest`).
- **Arrange, Act, Assert**: Structure each test method with `// Arrange`, `// Act`, and `// Assert` comments to separate setup, invocation, and verification.

### Wiremock
- **WireMock usage**: When using WireMock in tests, prefer calling `stubFor` and other stubbing methods on the injected `WireMockServer` instance (e.g. a field annotated with `@InjectWireMock`) rather than using the static `WireMock.stubFor(...)` API, so that stubs are scoped to the configured server instance.
- **WireMock response bodies**: Prefer using `.withBodyFile(...)` and JSON files under `src/test/resources/__files/` for stubbed HTTP responses instead of inline string bodies, so that payloads stay reusable and easy to maintain.

---

### Skills

- **Location**: `.agents/skills/`
- **Usage**: Each skill should be documented in its own file, explaining:
  - What the skill does.
  - When agents should use it.
  - Any configuration or assumptions.

You can start by adding markdown or JSON/YAML skill descriptions under `.agents/skills/`.