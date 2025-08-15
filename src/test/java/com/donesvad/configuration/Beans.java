package com.donesvad.configuration;

import com.donesvad.rest.client.ApiClient;
import com.donesvad.rest.client.TeamCityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
  @Bean
  public ApiClient apiClient(TestConfig cfg) {
    return new ApiClient(cfg.getUsername(), cfg.getPassword());
  }

  @Bean
  public TeamCityClient teamCityClient(ApiClient api) {
    return new TeamCityClient(api);
  }
}
