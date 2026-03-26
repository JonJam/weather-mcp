---
name: mcp-version-bake
overview: Bake the semantic-release version into the packaged `application.yaml` during the Gradle build so the published Docker image reports the correct MCP server version (instead of `local-SNAPSHOT`).
todos:
  - id: update-application-yaml-token
    content: In `src/main/resources/application.yaml`, change `spring.ai.mcp.server.version` from `${APP_VERSION:local-SNAPSHOT}` to a build-time token `@appVersion@`.
    status: completed
  - id: add-processresources-token-replace
    content: "In `build.gradle`, add `processResources` filtering for `application.yaml` using `org.apache.tools.ant.filters.ReplaceTokens`, sourcing `appVersion` from `System.getenv('APP_VERSION') ?: 'local-SNAPSHOT'` (and mark it as an input with `inputs.property`)."
    status: completed
  - id: ci-flow-check
    content: "Do a quick sanity check in CI logic: confirm semantic-release publish command continues to set `APP_VERSION=${nextRelease.version}` for `./gradlew jib` (it already does in `release.config.cjs`). No workflow change should be required if the Gradle filtering is implemented."
    status: completed
  - id: verify-built-image-version
    content: Verify by building an image with `APP_VERSION=<test>` and running it to confirm the MCP server reports that exact version (ensure it no longer falls back to `local-SNAPSHOT`).
    status: completed
isProject: false
---

## What’s happening now

- `src/main/resources/application.yaml` sets `spring.ai.mcp.server.version` to `${APP_VERSION:local-SNAPSHOT}`.
- When the Docker image runs on DockerHub, `APP_VERSION` is not set inside the container, so Spring falls back to `local-SNAPSHOT`.

## Fix approach (bake version at build time)

- Change `application.yaml` to use a build-time token (e.g. `@appVersion@`) rather than reading `APP_VERSION` at runtime.
- Update `build.gradle` so `processResources` replaces that token with:
  - `APP_VERSION` from the CI environment (semantic-release already runs `APP_VERSION=${nextRelease.version} ./gradlew jib ...`), or
  - `local-SNAPSHOT` for local/dev runs.

## Files to change

- `[src/main/resources/application.yaml]`: replace `version: ${APP_VERSION:local-SNAPSHOT}` with `version: @appVersion@`.
- `[build.gradle]`: add a `processResources` filter using `ReplaceTokens` to set `appVersion`.

## Why CI will pick up release tags

- In `release.config.cjs`, the semantic-release publish command already sets `APP_VERSION=${nextRelease.version}` for the Gradle `jib` task.
- With resource filtering, the resulting container will contain the correct `spring.ai.mcp.server.version` value.

## Verification

- Build locally with an explicit version (example): `APP_VERSION=1.2.3 ./gradlew jibDockerBuild`.
- Run the container (or inspect logs) and confirm the MCP `version` value equals `1.2.3`.
- Optionally confirm test profile remains unchanged via `application-test.yaml` (it currently hardcodes `test-SNAPSHOT`).

