package com.donesvad.rest.dto.agent;

import lombok.Value;

@Value
public class AgentEnabledRequest {
  boolean enabled;
  String comment;
}
