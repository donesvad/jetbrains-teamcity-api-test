package com.donesvad.rest.dto.project;

import lombok.Builder;

@Builder
public record CreateProjectRequest(
    ParentProject parentProject, String name, String id, boolean copyAllAssociatedSettings) {
  @Builder
  public record ParentProject(String locator) {}
}
