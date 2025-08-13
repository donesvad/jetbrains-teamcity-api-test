package com.donesvad.scenario;


import com.donesvad.configuration.TestConfig;
import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.dto.build.QueueBuildRequest;
import com.donesvad.rest.dto.settings.BuildTypesDto;
import com.donesvad.rest.service.TeamCityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportAndUsabilityTest extends BaseTest {

  @Autowired private TestConfig cfg;
  @Autowired private TeamCityClient client;
  @Autowired private TeamCityService svc;

  @Test
  void projectAndBuildTypesExistAndAreUsable() {
    // Project should exist (imported before), builds should be present
    var p = client.getProject(cfg.getProjectId());
    Assertions.assertEquals(
        cfg.getProjectId(), p.getId(), "Project not found: " + cfg.getProjectId());

    BuildTypesDto bts = client.getBuildTypesUnderProject(cfg.getProjectId());
    Assertions.assertTrue(
        bts.getCount() != null && bts.getCount() > 0, "No build types under " + cfg.getProjectId());

    // Queue a quick build to prove usability (compile)
    long id =
        svc.queueAndWaitSuccess(
            QueueBuildRequest.builder()
                .buildType(
                    QueueBuildRequest.BuildTypeRef.builder()
                        .id(cfg.getCompileBuildTypeId())
                        .build())
                .build(),
            120,
            5);
    Assertions.assertTrue(id > 0, "Build did not complete");
  }
}
