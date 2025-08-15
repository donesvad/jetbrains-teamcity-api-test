package com.donesvad.scenario;

import static org.apache.http.HttpStatus.SC_OK;

import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.dto.project.CreateProjectRequest;
import com.donesvad.rest.dto.vcs.CreateVcsRootRequest;
import com.donesvad.rest.dto.vcs.ProjectRef;
import com.donesvad.rest.dto.vcs.Properties;
import com.donesvad.rest.dto.vcs.Property;
import com.donesvad.rest.dto.vcs.VersionedSettingsConfigRequest;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportDslFlowTest extends BaseTest {

  @Autowired private TeamCityClient client;

  @BeforeEach
  void cleanIfProjectExists() {
    Response resp = client.getProjectResponse(config.getProjectId());
    if (resp.statusCode() == SC_OK) {
      client.deleteProject(config.getProjectId());
    }
  }

  @Test
  void importDslProject() {
    client.createProjectUnderRoot(
        CreateProjectRequest.builder()
            .parentProject(CreateProjectRequest.ParentProject.builder().locator("_Root").build())
            .name(config.getProjectId())
            .id(config.getProjectId())
            .copyAllAssociatedSettings(false)
            .build());

    var vcsId = "dsl_" + config.getProjectId() + "_git";
    List<Property> props = getProperties();
    var vcsReq =
        CreateVcsRootRequest.builder()
            .id(vcsId)
            .name("DSL Repo for " + config.getProjectId())
            .vcsName("jetbrains.git")
            .project(new ProjectRef(config.getProjectId()))
            .properties(new Properties(props))
            .build();
    String createdVcsId = client.createVcsRoot(vcsReq);

    var versionedSettingsConfig =
        VersionedSettingsConfigRequest.builder()
            .format("KOTLIN")
            .synchronizationMode("enabled")
            .allowUIEditing(false)
            .storeSecureValuesOutsideVcs(false)
            .portableDsl(true)
            .showSettingsChanges(true)
            .vcsRootId(createdVcsId)
            .buildSettingsMode("useFromVCS")
            .importDecision("importFromVCS")
            .build();
    client.putVersionedSettingsConfig(config.getProjectId(), versionedSettingsConfig);

    client.loadVersionedSettings(config.getProjectId());

    var project = client.getProject(config.getProjectId());
    Assertions.assertEquals(
        config.getProjectId(), project.getId(), "Project not found: " + config.getProjectId());
  }

  private List<Property> getProperties() {
    List<Property> props = new java.util.ArrayList<>();
    props.add(new Property("url", config.getDslRepoUrl()));
    props.add(new Property("branch", config.getDslRepoBranch()));
    props.add(new Property("authMethod", config.getVcsAuthMethod()));
    props.add(new Property("username", config.getVcsUsername()));
    props.add(new Property("secure:password", config.getVcsToken()));
    return props;
  }
}
