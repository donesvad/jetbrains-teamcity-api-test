package com.donesvad.scenario;

import com.donesvad.actions.ImportDslActions;
import com.donesvad.assertions.ImportDslAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportDslFlowTest extends BaseTest {

  @Autowired private ImportDslActions actions;
  @Autowired private ImportDslAssertions assertions;

  @BeforeEach
  void cleanIfProjectExists() {
    actions.ensureProjectAbsent(config.getProjectId());
  }

  @Test
  void importDslProject() {
    String projectId = config.getProjectId();
    actions.createProjectUnderRoot(projectId);
    String vcsId = actions.createDslVcsRoot(projectId);
    actions.enableVersionedSettings(projectId, vcsId);
    actions.waitDslIsApplied(projectId);
    assertions.assertProjectCreated(projectId);
    assertions.assertBuildTypesImported(projectId);
  }

  @Test
  void loadSettingsAfterImportDslProject() {
    String projectId = config.getProjectId();
    actions.createProjectUnderRoot(projectId);
    String vcsId = actions.createDslVcsRoot(projectId);
    actions.enableVersionedSettings(projectId, vcsId);
    actions.loadSettings(projectId);
    // TODO: it fails because the project is auto-removed in case user tries to reload setting right
    // after setting the version config
    // It looks like an issue, worth investigation
    assertions.assertProjectCreated(projectId);
    assertions.assertBuildTypesImported(projectId);
  }
}
