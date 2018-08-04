package com.apps.dashboard.controllers.config;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import com.apps.dashboard.services.ApplicationStatusService;
import java.util.Collections;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.apps.dashboard.controllers")
public class MockCoreConfig {

  @Bean
  public ApplicationService applicationService() {

    final Long appId = 123L;

    Application app = Application.builder()
        .id(appId)
        .name("Test")
        .build();

    ApplicationService applicationService = mock(ApplicationService.class);

    when(applicationService.getAllApplications())
        .thenReturn(Collections.singletonList(app));

    when(applicationService.getApplicationById(eq(appId)))
        .thenReturn(app);

    return applicationService;
  }

  @Bean
  LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }

  @Bean
  public ApplicationStatusService applicationStatusService() {
    return mock(ApplicationStatusService.class);
  }

}
