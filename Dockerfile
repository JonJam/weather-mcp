# Build stage: compile and package the application
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

ARG APP_VERSION=local-snapshot
ENV APP_VERSION=${APP_VERSION}

COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# Runtime stage: minimal image for running the MCP server
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1000 mcp && adduser -u 1000 -G mcp -D mcp
USER mcp

COPY --from=builder /app/build/libs/accuweather-mcp-*.jar app.jar

# MCP servers communicate via stdio; no exposed ports
ENTRYPOINT ["java", "-jar", "app.jar"]
