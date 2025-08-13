package com.donesvad.rest.client;

import static org.apache.http.HttpStatus.SC_OK;

import com.donesvad.rest.dto.agent.AgentAuthorizedRequest;
import com.donesvad.rest.dto.agent.AgentEnabledRequest;
import com.donesvad.rest.dto.agent.AgentsDto;
import com.donesvad.rest.dto.build.BuildDto;
import com.donesvad.rest.dto.build.QueueBuildRequest;
import com.donesvad.rest.dto.build.QueuedBuildDto;
import com.donesvad.rest.dto.project.CreateProjectRequest;
import com.donesvad.rest.dto.project.ProjectDto;
import com.donesvad.rest.dto.project.ProjectsDto;
import com.donesvad.rest.dto.settings.BuildTypeDto;
import com.donesvad.rest.dto.settings.BuildTypesDto;
import com.donesvad.rest.dto.settings.ParametersDto;
import com.donesvad.rest.dto.vcs.CreateVcsRootRequest;
import com.donesvad.rest.dto.vcs.VersionedSettingsConfigRequest;
import com.donesvad.rest.endpoints.TeamCityEndpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamCityClient {

  private final ApiClient api;

  // server
  private Response getServerInfoResponse() {
    return api.getJson(TeamCityEndpoints.SERVER_INFO);
  }

  public String getServerInfoRawJson() {
    return getServerInfoResponse().then().statusCode(SC_OK).extract().asString();
  }

  // projects
  public ProjectsDto getProjects() {
    return api.getJson(TeamCityEndpoints.PROJECTS)
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(ProjectsDto.class);
  }

  public Response getProjectResponse(String projectId) {
    return api.getJson(TeamCityEndpoints.projectById(projectId));
  }

  public ProjectDto getProject(String projectId) {
    return getProjectResponse(projectId).then().statusCode(SC_OK).extract().as(ProjectDto.class);
  }

  public ProjectDto createProjectUnderRoot(CreateProjectRequest req) {
    return api.postJson(TeamCityEndpoints.PROJECTS, req)
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(ProjectDto.class);
  }

  // vcs roots
  public String createVcsRoot(CreateVcsRootRequest req) {
    return api.postJson(TeamCityEndpoints.VCS_ROOTS, req)
        .then()
        .statusCode(SC_OK)
        .extract()
        .path("id");
  }

  // versioned settings
  public void putVersionedSettingsConfig(String projectId, VersionedSettingsConfigRequest cfg) {
    api.putJson(TeamCityEndpoints.projectVsConfig(projectId), cfg).then().statusCode(SC_OK);
  }

  public void loadVersionedSettings(String projectId) {
    api.postJson(TeamCityEndpoints.projectVsLoad(projectId)).then().statusCode(SC_OK);
  }

  // build types and params
  public BuildTypesDto getBuildTypesUnderProject(String projectId) {
    return api.getJson(TeamCityEndpoints.buildTypesUnderProject(projectId))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(BuildTypesDto.class);
  }

  public BuildTypeDto getBuildType(String btId) {
    return api.getJson(TeamCityEndpoints.buildTypeById(btId))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(BuildTypeDto.class);
  }

  public ParametersDto getParametersOfBuildType(String btId) {
    return api.getJson(TeamCityEndpoints.parametersOfBuildType(btId))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(ParametersDto.class);
  }

  public void setBuildTypeParameter(String btId, String paramName, String value) {
    api.putJson(TeamCityEndpoints.setParameterOfBuildType(btId, paramName), value)
        .then()
        .statusCode(SC_OK);
  }

  // queue build and builds
  public QueuedBuildDto queueBuild(QueueBuildRequest req) {
    return api.postJson(TeamCityEndpoints.BUILD_QUEUE, req)
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(QueuedBuildDto.class);
  }

  public BuildDto getBuildById(long buildId) {
    return api.getJson(TeamCityEndpoints.buildById(buildId))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(BuildDto.class);
  }

  public BuildDto[] getLastFinishedBuilds(String btId, int count) {
    return api.getJson(TeamCityEndpoints.lastFinishedForBuildType(btId, count))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(BuildDto[].class);
  }

  // logs
  public String getBuildLog(long buildId) {
    return api.getPlain(TeamCityEndpoints.downloadBuildLog(buildId))
        .then()
        .statusCode(SC_OK)
        .contentType(ContentType.TEXT.toString())
        .extract()
        .asString();
  }

  // agents
  public AgentsDto getConnectedAgents() {
    return api.getJson(TeamCityEndpoints.agentsConnected())
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(AgentsDto.class);
  }

  public void setAgentAuthorized(long agentId, AgentAuthorizedRequest req) {
    api.putJson(TeamCityEndpoints.agentAuthorizedInfo(agentId), req).then().statusCode(SC_OK);
  }

  public void setAgentEnabled(long agentId, AgentEnabledRequest req) {
    api.putJson(TeamCityEndpoints.agentEnabledInfo(agentId), req).then().statusCode(SC_OK);
  }
}
