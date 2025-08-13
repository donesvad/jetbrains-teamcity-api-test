package com.donesvad.rest.dto.build;

import lombok.Data;

@Data
public class BuildDto {
  private Long id;
  private String state; // queued, running, finished
  private String status; // SUCCESS, FAILURE, etc.
}
