package com.donesvad.rest.dto.vcs;

import lombok.Data;

/**
 * DTO for TeamCity versioned settings status endpoint: GET
 * /app/rest/projects/id:{projectId}/versionedSettings/status
 *
 * <p>Actual fields may vary between versions; keep optional fields to be resilient.
 */
@Data
public class VersionedSettingsStatusDto {
  private String message;
  private String errors;
}
