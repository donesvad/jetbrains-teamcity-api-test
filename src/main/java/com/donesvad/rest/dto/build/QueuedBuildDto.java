package com.donesvad.rest.dto.build;

import lombok.Data;

@Data
public class QueuedBuildDto {
  @Data
  public static class BuildTypeRef {
    private String id;
  }

  private Long id; // returned when you enqueue
  private BuildTypeRef buildType;
}
