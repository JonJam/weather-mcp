module.exports = {
  branches: [
    "main"
  ],
  plugins: [
    "@semantic-release/commit-analyzer",
    "@semantic-release/release-notes-generator",
    "@semantic-release/github",
    [
      "@semantic-release/exec",
      {
        "publishCmd": `APP_VERSION=\${nextRelease.version} ./gradlew jib -Djib.to.auth.username=${process.env.DOCKER_USERNAME} -Djib.to.auth.password=${process.env.DOCKER_PASSWORD} -Djib.to.tags=\${nextRelease.version},latest`
      }
    ],
    [
      "@semantic-release/exec",
      {
        "verifyReleaseCmd": "./mcp-publisher validate",
        "prepareCmd": `APP_VERSION=\${nextRelease.version} jq --arg v "$APP_VERSION" '.version = $v | (.packages |= map(.version = $v))' server.json > server.tmp && mv server.tmp server.json`,
        "publishCmd": "./mcp-publisher publish"
      }
    ],
  ]
}

