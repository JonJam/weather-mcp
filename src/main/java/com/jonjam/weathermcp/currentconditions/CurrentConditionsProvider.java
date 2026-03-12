package com.jonjam.weathermcp.currentconditions;

import com.jonjam.weathermcp.LocaleUtils;
import java.util.Locale;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;

// TODO Review and improve this
@Component
public class CurrentConditionsProvider {

  @McpPrompt(
      name = "current-conditions",
      description = "Ask for the current weather conditions in a location.")
  public String currentConditionsPrompt(
      @McpArg(name = "location", description = "City or point of interest", required = true)
          final String location,
      final McpMeta meta) {

    final Locale resolvedLanguage = LocaleUtils.resolveLocale(meta);

    final StringBuilder text = new StringBuilder("Provide the current weather conditions for ");
    text.append(location);
    text.append(" using language code ").append(resolvedLanguage.toLanguageTag());
    text.append(".");

    return text.toString();
  }
}
