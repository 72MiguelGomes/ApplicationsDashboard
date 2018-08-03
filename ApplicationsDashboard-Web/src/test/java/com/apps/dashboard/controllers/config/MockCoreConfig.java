package com.apps.dashboard.controllers.config;

import static org.mockito.Mockito.when;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Arrays;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.apps.dashboard.controllers")
public class MockCoreConfig {

  @Bean
  public ApplicationService applicationService() {

    ApplicationService applicationService = Mockito.mock(ApplicationService.class);


    when(applicationService.getAllApplications())
        .thenReturn(Arrays.asList(Application.builder()
            .id(123L)
            .name("Test")
            .build()));

    return applicationService;
  }

  @Bean
  LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }

  @Bean
  public ApplicationStatusService applicationStatusService() {
    return Mockito.mock(ApplicationStatusService.class);
  }

}
