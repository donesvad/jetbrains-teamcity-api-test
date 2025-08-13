package com.donesvad.rest.dto.agent;

import java.util.List;
import lombok.Data;

@Data
public class AgentsDto {
  @Data
  public static class Agent {
    private Long id;
    private String name;
  }

  private Integer count;
  private List<Agent> agent;
}
