package com.dramadownloader.common.util;

import org.apache.commons.codec.digest.DigestUtils;

public final class HashUtil {
  private HashUtil() {

  }

  public static String sha256(String input) {
    return DigestUtils.sha256Hex(input);
  }
}
