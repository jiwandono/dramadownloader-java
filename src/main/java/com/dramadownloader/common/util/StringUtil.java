package com.dramadownloader.common.util;

import java.text.Normalizer;

public final class StringUtil {
  private StringUtil() {

  }

  public static String toPrettyUrl(String string) {
    return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
        .replaceAll("[^\\p{Alnum}]+", "-");
  }

  public static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }
}
