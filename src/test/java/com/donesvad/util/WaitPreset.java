package com.donesvad.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WaitPreset {
  TIMEOUT(120_000L),
  SHORT_POLLING(250L),
  MEDIUM_POLLING(1_000L),
  LONG_POLLING(2_000L);

  private final long value;
}
