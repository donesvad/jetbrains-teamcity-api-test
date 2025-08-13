package com.donesvad.rest.service;

import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.dto.agent.AgentAuthorizedRequest;
import com.donesvad.rest.dto.agent.AgentEnabledRequest;
import com.donesvad.rest.dto.agent.AgentsDto;
import com.donesvad.rest.dto.build.BuildDto;
import com.donesvad.rest.dto.build.QueueBuildRequest;
import com.donesvad.rest.dto.build.QueuedBuildDto;
import com.donesvad.rest.dto.project.CreateProjectRequest;
import com.donesvad.rest.dto.project.ProjectDto;
import com.donesvad.rest.dto.settings.BuildTypesDto;
import com.donesvad.rest.dto.settings.ParametersDto;
import com.donesvad.rest.dto.vcs.CreateVcsRootRequest;
import com.donesvad.rest.dto.vcs.VersionedSettingsConfigRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamCityService {

  private final TeamCityClient client;

  // simple forwarders (service stays orchestration oriented)
  public ProjectDto createProjectUnderRoot(CreateProjectRequest req) {
    return client.createProjectUnderRoot(req);
  }

  public String createVcsRoot(CreateVcsRootRequest req) {
    return client.createVcsRoot(req);
  }

  public void putVersionedSettingsConfig(String projectId, VersionedSettingsConfigRequest cfg) {
    client.putVersionedSettingsConfig(projectId, cfg);
  }

  public void loadVersionedSettings(String projectId) {
    client.loadVersionedSettings(projectId);
  }

  public BuildTypesDto getBuildTypesUnderProject(String projectId) {
    return client.getBuildTypesUnderProject(projectId);
  }

  public ParametersDto getParametersOfBuildType(String btId) {
    return client.getParametersOfBuildType(btId);
  }

  public QueuedBuildDto queueBuild(QueueBuildRequest req) {
    return client.queueBuild(req);
  }

  public BuildDto getBuildById(long buildId) {
    return client.getBuildById(buildId);
  }

  public BuildDto[] getLastFinishedBuilds(String btId, int count) {
    return client.getLastFinishedBuilds(btId, count);
  }

  public String getBuildLog(long buildId) {
    return client.getBuildLog(buildId);
  }

  public AgentsDto getConnectedAgents() {
    return client.getConnectedAgents();
  }

  public void setAgentAuthorized(long agentId, AgentAuthorizedRequest req) {
    client.setAgentAuthorized(agentId, req);
  }

  public void setAgentEnabled(long agentId, AgentEnabledRequest req) {
    client.setAgentEnabled(agentId, req);
  }

  // example workflow that still receives DTOs from tests
  public long queueAndWaitSuccess(QueueBuildRequest queueReq, int tries, int sleepSeconds) {
    QueuedBuildDto queuedBuildDto = client.queueBuild(queueReq);
    long id = queuedBuildDto.getId();
    // TODO: consider using CompletableFuture to avoid blocking
    // and to allow parallel execution of other tasks while waiting for the build to finish
    // TODO: OR consider creating a separate wrapper for waiters
    for (int i = 0; i < tries; i++) {
      BuildDto buildDto = client.getBuildById(id);
      if ("finished".equalsIgnoreCase(buildDto.getState())) {
        if (!"SUCCESS".equalsIgnoreCase(buildDto.getStatus())) {
          throw new IllegalStateException(
              "Build finished with status " + buildDto.getStatus() + " id=" + id);
        }
        return id;
      }
      try {
        Thread.sleep(sleepSeconds * 1000L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    }
    throw new IllegalStateException("Timed out waiting for build " + id);
  }
}
