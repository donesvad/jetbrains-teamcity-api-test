package com.donesvad.configuration;

import com.donesvad.rest.client.ApiClient;
import com.donesvad.rest.client.TeamCityClient;
import com.donesvad.rest.service.TeamCityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
  @Bean
  public ApiClient apiClient(TestConfig cfg) {
    return new ApiClient(cfg.getBaseUrl(), cfg.getUsername(), cfg.getPassword());
  }

  @Bean
  public TeamCityClient teamCityClient(ApiClient api) {
    return new TeamCityClient(api);
  }

  @Bean
  public TeamCityService teamCityService(TeamCityClient client) {
    return new TeamCityService(client);
  }
}
