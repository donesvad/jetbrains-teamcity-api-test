package com.donesvad.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ProjectUtil {

  /** Generate a unique project id derived from a base id. */
  public static String generateUniqueProjectId(String baseId) {
    String suffix = RandomUtil.randomTcSafeSuffix();
    return baseId + "_" + suffix;
  }
}
