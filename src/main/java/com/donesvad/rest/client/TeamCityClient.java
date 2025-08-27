package com.donesvad.rest.client;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import com.donesvad.rest.dto.ParametersDto;
import com.donesvad.rest.dto.buildtype.BuildTypesDto;
import com.donesvad.rest.dto.project.CreateProjectRequest;
import com.donesvad.rest.dto.project.ProjectDto;
import com.donesvad.rest.dto.project.ProjectsDto;
import com.donesvad.rest.dto.vcs.CreateVcsRootRequest;
import com.donesvad.rest.dto.vcs.VersionedSettingsConfigRequest;
import com.donesvad.rest.dto.vcs.VersionedSettingsStatusDto;
import com.donesvad.rest.endpoints.TeamCityEndpoints;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@RequiredArgsConstructor
public class TeamCityClient {

  private final ApiClient api;

  public Response getProjectResponse(String projectId) {
    return api.get(TeamCityEndpoints.projectById(projectId));
  }

  public ProjectDto getProject(String projectId) {
    return getProjectResponse(projectId).then().statusCode(SC_OK).extract().as(ProjectDto.class);
  }

  public BuildTypesDto getProjectBuildTypes(String projectId) {
    return api.get(TeamCityEndpoints.projectBuildTypes(projectId))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(BuildTypesDto.class);
  }

  public ProjectsDto getProjects() {
    return api.get(TeamCityEndpoints.PROJECTS)
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(ProjectsDto.class);
  }

  public void createProjectUnderRoot(CreateProjectRequest req) {
    api.post(TeamCityEndpoints.PROJECTS, req)
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(ProjectDto.class);
  }

  public void deleteProject(String projectId) {
    api.delete(TeamCityEndpoints.projectById(projectId))
        .then()
        .statusCode(anyOf(equalTo(SC_OK), equalTo(SC_NO_CONTENT)));
  }

  public String createVcsRoot(CreateVcsRootRequest req) {
    return api.post(TeamCityEndpoints.VCS_ROOTS, req).then().statusCode(SC_OK).extract().path("id");
  }

  public void putVersionedSettingsConfig(String projectId, VersionedSettingsConfigRequest cfg) {
    Response response = api.put(TeamCityEndpoints.projectVsConfig(projectId), cfg);
    response.then().statusCode(SC_OK);
  }

  public void loadVersionedSettings(String projectId) {
    api.post(TeamCityEndpoints.projectVsLoad(projectId)).then().statusCode(SC_OK);
  }

  public Response getVersionedSettingsStatusResponse(String projectId) {
    return api.get(TeamCityEndpoints.versionedSettingsStatus(projectId));
  }

  public VersionedSettingsStatusDto getVersionedSettingsStatus(String projectId) {
    return getVersionedSettingsStatusResponse(projectId)
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(VersionedSettingsStatusDto.class);
  }

  public ParametersDto getBuildTypeParameters(String buildTypeId) {
    return api.get(TeamCityEndpoints.buildTypeParameters(buildTypeId))
        .then()
        .statusCode(SC_OK)
        .extract()
        .as(ParametersDto.class);
  }
}
