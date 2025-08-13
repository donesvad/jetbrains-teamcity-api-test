package com.donesvad.rest.dto.agent;

import lombok.Value;

@Value
public class AgentAuthorizedRequest {
  boolean authorized;
  String comment;
}
