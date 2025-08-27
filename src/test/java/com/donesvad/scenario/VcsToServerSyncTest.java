package com.donesvad.scenario;

import static com.donesvad.util.ProjectUtil.generateUniqueProjectId;

import com.donesvad.actions.ImportDslActions;
import com.donesvad.actions.VcsSyncActions;
import com.donesvad.assertions.ImportDslAssertions;
import com.donesvad.util.WaitPreset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** VCS -> Server synchronization test. */
public class VcsToServerSyncTest extends BaseTest {

  @Autowired private ImportDslActions actions;
  @Autowired private VcsSyncActions syncActions;
  @Autowired private ImportDslAssertions assertions;

  private String projectId;

  @BeforeEach
  void generateProjectId() {
    String baseProjectId = config.getProjectId();
    projectId = generateUniqueProjectId(baseProjectId);
  }

  @AfterEach
  void cleanUpProjectId() {
    actions.ensureProjectAbsent(projectId);
    // Cleanup temporary Git branches and working directories created during the test
    syncActions.cleanupTempArtifacts();
  }

  @Test
  void syncChangeFromVcsIsAppliedOnServer() throws Exception {
    int originalParametersNumber = 4;
    String projectName = projectId + " Name";
    String tempBranchRef = syncActions.createAndPushTempBranchFromSource();
    actions.createProjectUnderRoot(projectId, projectName);
    String vcsId = actions.createDslVcsRoot(projectId, tempBranchRef);
    actions.enableVersionedSettings(projectId, vcsId);
    actions.waitDslIsApplied(projectId);
    assertions.assertBuildTypeParamCount(projectId + "_Build", originalParametersNumber);
    syncActions.pushChangeToBranch(tempBranchRef, "/vcs-sync/updated-Build.txt");
    actions.waitDslIsApplied(projectId);
    actions.loadSettings(projectId);
    assertions.awaitBuildTypeParamCount(
        projectId + "_Build",
        originalParametersNumber - 1,
        WaitPreset.TIMEOUT,
        WaitPreset.MEDIUM_POLLING);
  }
}
