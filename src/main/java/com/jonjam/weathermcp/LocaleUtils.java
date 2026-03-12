package com.jonjam.weathermcp;

import java.util.Locale;
import org.jspecify.annotations.Nullable;
import org.springaicommunity.mcp.annotation.McpMeta;
import org.springframework.util.StringUtils;

public final class LocaleUtils {

  private static final Locale DEFAULT_LOCALE = Locale.US;
  private static final String LOCALE_KEY = "locale";

  private LocaleUtils() {}

  public static Locale resolveLocale(final McpMeta meta) {
    Locale resolvedLanguage = DEFAULT_LOCALE;

    final @Nullable String metaLocale = (String) meta.get(LOCALE_KEY);
    if (StringUtils.hasText(metaLocale)) {
      final Locale parsed = Locale.forLanguageTag(metaLocale);
      final String language = parsed.getLanguage();
      final String country = parsed.getCountry();

      if (!language.isEmpty() && !country.isEmpty()) {
        resolvedLanguage = parsed;
      }
    }

    return resolvedLanguage;
  }
}
