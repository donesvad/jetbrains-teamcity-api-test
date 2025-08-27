package com.donesvad.actions;

import static org.apache.http.HttpStatus.SC_OK;

import com.donesvad.configuration.TestConfig;
import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.client.VersionedSettingsWaitStatus;
import com.donesvad.rest.dto.project.CreateProjectRequest;
import com.donesvad.rest.dto.vcs.CreateVcsRootRequest;
import com.donesvad.rest.dto.vcs.ProjectRef;
import com.donesvad.rest.dto.vcs.Properties;
import com.donesvad.rest.dto.vcs.Property;
import com.donesvad.rest.dto.vcs.VersionedSettingsConfigRequest;
import com.donesvad.util.WaitPreset;
import com.donesvad.wait.VersionedSettingsWaiter;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Business-level reusable actions for importing a TeamCity project from DSL. This sits between
 * tests and low-level REST client to keep tests concise and expressive.
 */
@Component
@RequiredArgsConstructor
public class ImportDslActions {

  private final TeamCityClient client;
  private final VersionedSettingsWaiter waiter;
  private final TestConfig config;

  /** Delete project if exists. */
  public void ensureProjectAbsent(String projectId) {
    Response resp = client.getProjectResponse(projectId);
    if (resp.statusCode() == SC_OK) {
      client.deleteProject(projectId);
    }
  }

  /** Delete all projects whose id starts with the given prefix. */
  public void ensureProjectsWithPrefixAbsent(String projectIdPrefix) {
    var projects = client.getProjects();
    if (projects != null && projects.getProject() != null) {
      for (var p : projects.getProject()) {
        if (p != null && p.getId() != null && p.getId().startsWith(projectIdPrefix)) {
          client.deleteProject(p.getId());
        }
      }
    }
  }

  /** Create an empty project under _Root. */
  public void createProjectUnderRoot(String projectId, String projectName) {
    client.createProjectUnderRoot(
        CreateProjectRequest.builder()
            .parentProject(CreateProjectRequest.ParentProject.builder().locator("_Root").build())
            .name(projectName)
            .id(projectId)
            .copyAllAssociatedSettings(true)
            .build());
  }

  /**
   * Create a Git VCS root for DSL using values from TestConfig. Returns the created VCS root id.
   */
  public String createDslVcsRoot(String projectId) {
    return createDslVcsRoot(projectId, config.getDslRepoBranch());
  }

  /** Create a Git VCS root for DSL using a specific branch reference (refs/heads/...). */
  public String createDslVcsRoot(String projectId, String branchRef) {
    String vcsId = "dsl_" + projectId + "_git";
    List<Property> props = new ArrayList<>();
    props.add(new Property("url", config.getDslRepoUrl()));
    props.add(new Property("branch", branchRef));
    props.add(new Property("authMethod", config.getVcsAuthMethod()));
    props.add(new Property("username", config.getVcsUsername()));
    props.add(new Property("secure:password", config.getVcsToken()));

    var req =
        CreateVcsRootRequest.builder()
            .id(vcsId)
            .name("DSL Repo for " + projectId)
            .vcsName("jetbrains.git")
            .project(new ProjectRef(projectId))
            .properties(new Properties(props))
            .build();
    return client.createVcsRoot(req);
  }

  /** Enable versioned settings pointing to provided VCS root. */
  public void enableVersionedSettings(String projectId, String vcsRootId) {
    var cfg =
        VersionedSettingsConfigRequest.builder()
            .format("kotlin")
            .synchronizationMode("enabled")
            .allowUIEditing(true)
            .storeSecureValuesOutsideVcs(true)
            .portableDsl(true)
            .showSettingsChanges(true)
            .vcsRootId(vcsRootId)
            .buildSettingsMode("alwaysUseCurrent")
            .importDecision("importFromVCS")
            .build();
    client.putVersionedSettingsConfig(projectId, cfg);
  }

  /** Wait for Applied Changes status. */
  public void waitDslIsApplied(String projectId) {
    waiter.waitForVersionedSettingsStatus(
        projectId,
        VersionedSettingsWaitStatus.APPLIED_CHANGES,
        WaitPreset.TIMEOUT,
        WaitPreset.LONG_POLLING);
  }

  /** Trigger manual load of versioned settings. */
  public void loadSettings(String projectId) {
    client.loadVersionedSettings(projectId);
    waitDslIsApplied(projectId);
  }
}
