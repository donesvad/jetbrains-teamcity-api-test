package com.donesvad.rest.dto.project;

import java.util.List;

import lombok.Data;

@Data
public class ProjectsDto {
  private Integer count;
  private List<ProjectDto> project;
}
