package com.jonjam.weathermcp.currentconditions;

import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;

// TODO Review
@Component
public class CurrentConditionsPromptProvider {

  @McpPrompt(
      name = "current-conditions",
      description = "Ask for the current weather conditions in a location.")
  public String currentConditions(
      @McpArg(name = "location", description = "City or point of interest", required = true)
          String location,
      @McpArg(name = "language", description = "AccuWeather language code", required = false)
          String language) {

    StringBuilder text = new StringBuilder("Provide the current weather conditions for ");
    text.append(location);
    if (language != null && !language.isBlank()) {
      text.append(" using language code ").append(language);
    }
    text.append(".");

    return text.toString();
  }
}

