package com.donesvad.rest.dto.build;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QueueBuildRequest {
  @Value
  @Builder
  public static class BuildTypeRef {
    String id;
  }

  BuildTypeRef buildType;
}
