package com.donesvad.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import com.donesvad.rest.client.TeamCityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Centralized business assertions for TeamCity DSL import scenarios. */
@Component
@RequiredArgsConstructor
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
}
