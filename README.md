

# accuweather-mcp

- [Setup](#setup)
- [Run](#run)
- [Testing](#testing)
  - [MCP Inspector](#mcp-inspector)
  - [AI chat (Claude, Cursor)](#ai-chat-claude-cursor)
  - [Docker](#docker)
- [Patterns and practices](#patterns-and-practies)
  - [Git](#git)
  - [Spotless](#spotless)
  - [Checkstyle](#checkstyle)
  - [MCP](#mcp)
- [MCP Registry](#mcp-registry)
- [Future improvements](#future-improvements)


## Setup

- Define the following environment variables in your system
```
export ACCUWEATHER_API_KEY=replace-with-your-api-key
```

- Define a `application-local.yaml` in `src/main/resources/application.yaml` with the following content:

```YAML
logging:
  console:
    enabled: true
```

- If using Cursor, add the following documentation sources:
  - [Spring AI documentation](https://docs.spring.io/spring-ai/reference/)
  - [Wiremock documentation][[https://wiremock.org/docs/]](https://wiremock.org/docs/]) 
  - [Lombok documentation][[https://projectlombok.org/features/]](https://projectlombok.org/features/]) 

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

## Patterns and practies
More information about patterns and practises for this project can be found in AGENTS.md.

### Git

This project uses [semantic-release](https://semantic-release.gitbook.io/semantic-release) to automate versioning, changelog generation, and publishing. 

**Important:** Commit messages that are intended to trigger a release must be prefixed according to the [Conventional Commits](https://semantic-release.gitbook.io/semantic-release#commit-message-format) standard. For example:

- `feat: add new forecast endpoint`
- `fix: correct hourly forecast time calculation`

### Spotless

This project uses the Spotless Gradle plugin to enforce a consistent Java style.

- `./gradlew spotlessApply` — format sources and fix style issues.
- `./gradlew spotlessCheck` — verify formatting without changing files.

### Checkstyle

Static analysis and broader code-quality checks are handled by Checkstyle using a Google-style-based configuration (with formatting delegated to Spotless):

- `./gradlew checkstyleMain` — run Checkstyle over main sources.
- `./gradlew checkstyleTest` — run Checkstyle over test sources.

### MCP
- **Tools** Ensure to handle errors (i.e. validation) according to the [specification](https://modelcontextprotocol.io/specification/2025-11-25/server/tools#error-handling)

## MCP Registry

This server is published to the [MCP Registry](https://modelcontextprotocol.io/registry). 

When using an MCP client that supports the registry (e.g. Cursor, Claude Desktop), you can add `io.github.jonjam/accuweather-mcp` from the registry and configure your AccuWeather API key as the `ACCUWEATHER_API_KEY` environment variable.

## Future improvements