package com.donesvad.rest.dto.project;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateProjectRequest {
  @Value
  @Builder
  public static class ParentProject {
    String locator;
  }

  ParentProject parentProject;
  String name;
  String id;
  boolean copyAllAssociatedSettings;
}
