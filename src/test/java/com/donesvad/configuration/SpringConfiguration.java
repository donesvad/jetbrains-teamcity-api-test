package com.donesvad.configuration;

import com.donesvad.util.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ComponentScan(basePackages = {"com.donesvad"})
@PropertySource(
    value = {
      "classpath:application-${environment:dev}.yml",
      "classpath:test-data-${environment:dev}.yml"
    },
    factory = YamlPropertySourceFactory.class)
@EnableConfigurationProperties({TestConfig.class})
@ContextConfiguration
public class SpringConfiguration {

  //    @Bean
  //    public ApiClient apiClient(TestConfig cfg) {
  //        return new ApiClient(cfg.getBaseUrl(), cfg.getUsername(), cfg.getPassword());
  //    }
  //
  //    @Bean
  //    public TeamCityClient teamCityClient(ApiClient api) {
  //        return new TeamCityClient(api);
  //    }
  //
  //    @Bean
  //    public TeamCityService teamCityService(TeamCityClient client) {
  //        return new TeamCityService(client);
  //    }

  //  @Bean
  //  public ObjectMapper objectMapper() {
  //    ObjectMapper objectMapper = new ObjectMapper();
  //    objectMapper.registerModule(new JavaTimeModule());
  //    objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
  //    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
  //    objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
  //    objectMapper.configure(WRITE_ENUMS_USING_TO_STRING, true);
  //    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
  //    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  //    return objectMapper;
  //  }
}
