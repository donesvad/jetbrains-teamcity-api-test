package com.donesvad.rest.dto.settings;

import lombok.Data;

@Data
public class BuildTypeDto {
  @Data
  public static class ProjectRef {
    private String id;
  }

  private String id;
  private String name;
  private ProjectRef project;
}
