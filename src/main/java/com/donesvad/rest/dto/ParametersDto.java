package com.donesvad.rest.dto;

import com.donesvad.rest.dto.vcs.Property;
import java.util.List;
import lombok.Data;

/**
 * Generic Parameters DTO for endpoints like /app/rest/buildTypes/id:{id}/parameters
 */
@Data
public class ParametersDto {
  private Integer count;
  private List<Property> property;
}
