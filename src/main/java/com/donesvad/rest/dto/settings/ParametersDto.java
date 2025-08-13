package com.donesvad.rest.dto.settings;

import java.util.List;

import lombok.Data;

@Data
public class ParametersDto {
  private List<ParameterPropertyDto> property;
}
