package com.donesvad.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.util.WaitPreset;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

/** Centralized business assertions for TeamCity DSL import scenarios. */
@Component
@RequiredArgsConstructor
@CommonsLog
public class ImportDslAssertions {

  private final TeamCityClient client;

  /** Assert only that the project exists (created) by checking its id and expected name. */
  public void assertProjectCreated(String projectId, String projectName) {
    var project = client.getProject(projectId);
    assertThat(project).as("Project not found: %s", projectId).isNotNull();
    assertThat(project.getId()).as("Project id mismatch").isEqualTo(projectId);
    assertThat(project.getName()).as("Project name mismatch").isEqualTo(projectName);
  }

  /** Assert that the project has build configurations imported from DSL with valid metadata. */
  public void assertBuildTypesImported(String projectId) {
    var buildTypes = client.getProjectBuildTypes(projectId);
    assertThat(buildTypes).as("Build types response is null after DSL import").isNotNull();
    assertThat(buildTypes.getCount()).as("Build types count is null after DSL import").isNotNull();
    assertThat(buildTypes.getCount())
        .as("No build configurations were imported from DSL for project: %s", projectId)
        .isGreaterThan(0);
    assertThat(buildTypes.getBuildType())
        .as("Build types list is null after DSL import for project: %s", projectId)
        .isNotNull()
        .isNotEmpty()
        // size should match count reported by API
        .hasSize(buildTypes.getCount())
        // each build type should have non-blank id and name
        .allSatisfy(
            bt -> {
              assertThat(bt.getId())
                  .as("BuildType id should be non-blank")
                  .isNotNull()
                  .isNotBlank();
              assertThat(bt.getName())
                  .as("BuildType name should be non-blank")
                  .isNotNull()
                  .isNotBlank();
            });
  }

  /** Assert the number of parameters for a specific build type equals expected. */
  public void assertBuildTypeParamCount(String buildTypeId, int expectedCount) {
    var params = client.getBuildTypeParameters(buildTypeId);
    assertThat(params).as("Parameters response is null for buildType %s", buildTypeId).isNotNull();
    Integer count = params.getCount();
    assertThat(count).as("Parameter count is null for buildType %s", buildTypeId).isNotNull();
    assertThat(count)
        .as("Unexpected parameter count for buildType %s", buildTypeId)
        .isEqualTo(expectedCount);
  }

  /**
   * Wait (with retries) until the build type parameters count equals expected. Polls the REST API
   * periodically and asserts at the end if the expected value was not reached.
   */
  public void awaitBuildTypeParamCount(
      String buildTypeId, int expectedCount, WaitPreset timeoutMs, WaitPreset pollIntervalMs) {
    long deadline = System.currentTimeMillis() + timeoutMs.getValue();
    Integer lastCount = null;
    int attempt = 0;
    while (System.currentTimeMillis() < deadline) {
      attempt++;
      var params = client.getBuildTypeParameters(buildTypeId);
      if (params != null) {
        lastCount = params.getCount();
      }
      log.info(
          String.format(
              "[awaitBuildTypeParamCount] buildTypeId=%s, expected=%d, attempt=%d, observed=%s",
              buildTypeId, expectedCount, attempt, lastCount));
      if (lastCount != null && lastCount == expectedCount) {
        log.info(
            String.format(
                "[awaitBuildTypeParamCount] SUCCESS: buildTypeId=%s reached expected count=%d after %d attempts",
                buildTypeId, expectedCount, attempt));
        return; // reached expected
      }
      try {
        Thread.sleep(pollIntervalMs.getValue());
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted while waiting for build type parameter count", ie);
      }
    }
    assertThat(lastCount)
        .as(
            "Timed out waiting for parameter count %s for buildType %s. Last observed count: %s",
            expectedCount, buildTypeId, lastCount)
        .isEqualTo(expectedCount);
  }
}
