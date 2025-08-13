package com.donesvad.rest.dto.settings;

import java.util.List;

import lombok.Data;

@Data
public class BuildTypesDto {
  private Integer count;
  private List<BuildTypeDto> buildType;
}
