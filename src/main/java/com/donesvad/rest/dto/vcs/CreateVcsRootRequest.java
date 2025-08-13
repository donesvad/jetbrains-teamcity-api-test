package com.donesvad.rest.dto.vcs;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateVcsRootRequest {
  @Value
  @Builder
  public static class ProjectRef {
    String id;
  }

  @Value
  @Builder
  public static class Properties {
    List<Property> property;
  }

  @Value
  @Builder
  public static class Property {
    String name;
    String value;
  }

  String id;
  String name;
  String vcsName;
  ProjectRef project;
  Properties properties;
}
