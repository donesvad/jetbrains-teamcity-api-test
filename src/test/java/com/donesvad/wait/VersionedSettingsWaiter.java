package com.donesvad.wait;

import static org.assertj.core.api.Assertions.assertThat;

import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.client.VersionedSettingsWaitStatus;
import com.donesvad.rest.dto.vcs.VersionedSettingsStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

/**
 * Polling-based waiter for TeamCity Versioned Settings statuses. Keeps retry/wait logic outside of
 * the REST client so the client stays focused on pure API calls.
 */
@CommonsLog
@Component
@RequiredArgsConstructor
public class VersionedSettingsWaiter {

  private final TeamCityClient client;

  /**
   * Waits until the versioned settings status contains the selected phrase. Polls REST endpoint:
   * /app/rest/projects/id:{projectId}/versionedSettings/status and returns only when the response
   * body contains the phrase associated with the given status.
   */
  public void waitForVersionedSettingsStatus(
      String projectId, VersionedSettingsWaitStatus status, long timeoutMs, long pollIntervalMs) {
    long deadline = System.currentTimeMillis() + timeoutMs;
    VersionedSettingsStatusDto dto = client.getVersionedSettingsStatusResponse(projectId);
    int attempt = 0;
    while (System.currentTimeMillis() < deadline) {
      attempt++;
      dto = client.getVersionedSettingsStatusResponse(projectId);
      String text = dto != null ? dto.getMessage() : null;
      log.info(
          String.format(
              "[waitForVersionedSettingsStatus] projectId=%s, targetStatus=%s, attempt=%d, text='%s'",
              projectId, status.name(), attempt, text));
      if (text != null && text.contains(status.phrase())) {
        log.info(
            String.format(
                "[waitForVersionedSettingsStatus] SUCCESS: projectId=%s reached status='%s' after %d attempts",
                projectId, status.phrase(), attempt));
        return;
      }
      try {
        Thread.sleep(pollIntervalMs);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(
            "Interrupted while waiting for versioned settings status: " + status, ie);
      }
    }
    String actualMessage = (dto == null ? null : dto.getMessage());
    assertThat(dto)
        .as(
            "Versioned settings status DTO is null after waiting for status '%s' for project '%s'",
            status.name(), projectId)
        .isNotNull();
    assertThat(actualMessage)
        .as(
            "Versioned settings status message does not contain expected phrase '%s' for project '%s'. Actual: '%s'",
            status.phrase(), projectId, actualMessage)
        .contains(status.phrase());
  }
}
