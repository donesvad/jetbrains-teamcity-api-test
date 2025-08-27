package com.donesvad.util;

import lombok.experimental.UtilityClass;

/**
 * Utility methods for generating random strings for tests. Centralizes UUID-based randomness to
 * keep call sites clean and consistent.
 */
@UtilityClass
public final class RandomUtil {

  /**
   * Returns a lowercase hex string (without hyphens) derived from a UUID, truncated to the given
   * length.
   */
  public static String randomHex(int length) {
    String hex = java.util.UUID.randomUUID().toString().replace("-", "").toLowerCase();
    if (length <= 0) return "";
    if (length >= hex.length()) return hex;
    return hex.substring(0, length);
  }

  /** Returns an 8-character TeamCity-safe suffix suitable for IDs/names. */
  public static String randomTcSafeSuffix() {
    return randomHex(8);
  }

  /** Generates a random branch name starting with the given prefix and an 8-char hex suffix. */
  public static String randomBranchName(String prefix) {
    String p = (prefix == null) ? "" : prefix;
    return p + randomTcSafeSuffix();
  }
}
