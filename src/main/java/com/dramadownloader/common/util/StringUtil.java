package com.dramadownloader.common.util;

import org.apache.commons.lang.StringUtils;

import java.text.Normalizer;

public final class StringUtil {
  private StringUtil() {

  }

  public static String urlize(String string) {
    return StringUtils.strip(
        Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
        .replaceAll("[^\\p{Alnum}]+", "-")
    , "-");
  }

  public static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }
}
