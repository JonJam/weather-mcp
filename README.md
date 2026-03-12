# weather-mcp

## Setup
- Define a `application-local.yaml` in `src/main/resources/application.yaml`

## Run
- Run with `SPRING_PROFILES_ACTIVE=local ./gradlew bootRun`

## Testing
- To test with MCP Inspector, use the following command:

```bash
SPRING_PROFILES_ACTIVE=local \
npx @modelcontextprotocol/inspector \
  -e 'JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005' \
  java -jar /Users/jonjam/dev/weather-mcp/build/libs/weathermcp-0.0.1-snapshot.jar
```

## Code style and static analysis

### Spotless

This project uses the Spotless Gradle plugin to enforce a consistent Java style.

- `./gradlew spotlessApply` — format sources and fix style issues.
- `./gradlew spotlessCheck` — verify formatting without changing files.

### Checkstyle

Static analysis and broader code-quality checks are handled by Checkstyle using a Google-style-based configuration (with formatting delegated to Spotless):

- `./gradlew checkstyleMain` — run Checkstyle over main sources.
- `./gradlew checkstyleTest` — run Checkstyle over test sources.

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

## Documentation
- Add [Spring AI documentation](https://docs.spring.io/spring-ai/reference/) as a Docs source to Cursor.
- Add [Wiremock documentation][https://wiremock.org/docs/] as a Docs source to Cursor.
- Add [Lombok documentation][https://projectlombok.org/features/] as a Docs source to Cursor.


More information about patterns and practises for this project can be found in AGENTS.md.