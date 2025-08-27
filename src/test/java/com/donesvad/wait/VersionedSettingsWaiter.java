package com.donesvad.wait;

import static org.assertj.core.api.Assertions.assertThat;

import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.client.VersionedSettingsWaitStatus;
import com.donesvad.rest.dto.vcs.VersionedSettingsStatusDto;
import com.donesvad.util.WaitPreset;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.http.HttpStatus;
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
      String projectId,
      VersionedSettingsWaitStatus status,
      WaitPreset timeoutMs,
      WaitPreset pollIntervalMs) {
    long deadline = System.currentTimeMillis() + timeoutMs.getValue();
    Response response = null;
    int attempt = 0;
    while (System.currentTimeMillis() < deadline) {
      attempt++;
      response = client.getVersionedSettingsStatusResponse(projectId);
      log.info(
          String.format(
              "[waitForVersionedSettingsStatus] projectId=%s, targetStatus=%s, attempt=%d, text='%s'",
              projectId, status.name(), attempt, response.getBody().print()));
      if (response.getStatusCode() == HttpStatus.SC_OK) {
        VersionedSettingsStatusDto dto = response.as(VersionedSettingsStatusDto.class);
        String text = dto != null ? dto.getMessage() : null;

        if (text != null && text.contains(status.phrase())) {
          log.info(
              String.format(
                  "[waitForVersionedSettingsStatus] SUCCESS: projectId=%s reached status='%s' after %d attempts",
                  projectId, status.phrase(), attempt));
          return;
        }
      }
      if (response.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
        break;
      }

      try {
        Thread.sleep(pollIntervalMs.getValue());
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(
            "Interrupted while waiting for versioned settings status: " + status, ie);
      }
    }
    String actualMessage = (response == null ? null : response.getBody().print());
    assertThat(response)
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
