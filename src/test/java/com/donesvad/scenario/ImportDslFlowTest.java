package com.donesvad.scenario;

import com.donesvad.actions.ImportDslActions;
import com.donesvad.assertions.ImportDslAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportDslFlowTest extends BaseTest {

  @Autowired private ImportDslActions actions;
  @Autowired private ImportDslAssertions assertions;

  private String projectId;

  @BeforeEach
  void generateProjectId() {
    String baseProjectId = config.getProjectId();
    projectId = actions.generateUniqueProjectId(baseProjectId);
  }

  @AfterEach
  void cleanUpProjectId() {
    actions.ensureProjectAbsent(projectId);
  }

  @Test
  void importDslProject() {
    String projectName = projectId + " Name";
    actions.createProjectUnderRoot(projectId, projectName);
    String vcsId = actions.createDslVcsRoot(projectId);
    actions.enableVersionedSettings(projectId, vcsId);
    actions.waitDslIsApplied(projectId);
    assertions.assertProjectCreated(projectId, projectName);
    assertions.assertBuildTypesImported(projectId);
  }

  @Test
  void loadSettingsAfterImportDslProject() {
    String projectName = projectId + " Name";
    actions.createProjectUnderRoot(projectId, projectName);
    String vcsId = actions.createDslVcsRoot(projectId);
    actions.enableVersionedSettings(projectId, vcsId);
    actions.loadSettings(projectId);
    // TODO: it fails because the project is auto-removed in case user tries to reload setting right
    // after setting the version config
    // It looks like an issue, worth investigation
    assertions.assertProjectCreated(projectId, projectName);
    assertions.assertBuildTypesImported(projectId);
  }
}
