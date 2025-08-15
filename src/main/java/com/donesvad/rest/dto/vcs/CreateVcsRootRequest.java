package com.donesvad.rest.dto.vcs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateVcsRootRequest {

  String id;
  String name;
  String vcsName;
  ProjectRef project;
  Properties properties;
}
