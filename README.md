# accuweather-mcp

## Setup

```
export ACCUWEATHER_API_KEY=replace-with-your-api-key
```

- Define the following environment variables in your system
- Define a `application-local.yaml` in `src/main/resources/application.yaml` with the following content:

```YAML
logging:
  console:
    enabled: true
```

## Run

- Run with `SPRING_PROFILES_ACTIVE=local ./gradlew bootRun`

## Testing
**Note**: If you enable the Java debugger, it will produce output to standard out which will trigger errors in the stdio MCP protocol.

### MCP Inspector

To test with [MCP Inspector]([https://modelcontextprotocol.io/docs/tools/inspector](https://modelcontextprotocol.io/docs/tools/inspector)), run the following from the root of the repo:

```bash
npx @modelcontextprotocol/inspector -e 'JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005' java -jar "build/libs/accuweather-mcp-local-snapshot.jar"
```

This was sourced from this [blog.](https://medium.com/@tsteidle/creating-an-mcp-server-with-spring-boot-setup-debugging-and-unit-testing-8edbac9da5a6)

### AI chat (Claude, Cursor)

1. Add this configuration to the MCP settings:

```json
{
  "mcpServers": {
    "accuweather-mcp": {
      "command": "PATH_TO_JAVA",
      "args": ["-jar", "ABSOLUTE_PATH_TO_REPO/build/libs/accuweather-mcp-local-snapshot.jar"],
      "env": {
        "ACCUWEATHER_API_KEY": "API_KEY"
      }
    }
  }
}
```

If you are using SDKMAN, `command` should be `~/.sdkman/candidates/java/current/bin/java`

This was sourced from the [MCP docs](https://modelcontextprotocol.io/docs/develop/build-server#testing-your-server-with-claude-for-desktop-3).

Example user prompt:

```
Use the accuweather-mcp to look up the current weather in Manchester, UK.
```

### Docker

This project uses the [Jib Gradle plugin](https://github.com/GoogleContainerTools/jib) to build a Docker image.

- Build and load the image into your local Docker daemon:

```bash
./gradlew jibDockerBuild
```

The default image name is `jonjam/accuweather-mcp`.

To use the docker image with MCP Inspector, the command looks as follows:

```bash
npx @modelcontextprotocol/inspector docker run --rm -i --env "ACCUWEATHER_API_KEY=$ACCUWEATHER_API_KEY" jonjam/accuweather-mcp:latest
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
- Add [Wiremock documentation][[https://wiremock.org/docs/]](https://wiremock.org/docs/]) as a Docs source to Cursor.
- Add [Lombok documentation][[https://projectlombok.org/features/]](https://projectlombok.org/features/]) as a Docs source to Cursor.

More information about patterns and practises for this project can be found in AGENTS.md.

## MCP

### Tools

- Ensure to handle errors (i.e. validation) according to the [specification](https://modelcontextprotocol.io/specification/2025-11-25/server/tools#error-handling)
