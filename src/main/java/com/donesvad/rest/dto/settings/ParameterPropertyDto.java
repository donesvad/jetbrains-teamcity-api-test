package com.donesvad.rest.dto.settings;

import lombok.Data;

@Data
public class ParameterPropertyDto {
  @Data
  public static class Type {
    private String rawValue;
  }

  private String name;
  private String value;
  private Type type;
}
