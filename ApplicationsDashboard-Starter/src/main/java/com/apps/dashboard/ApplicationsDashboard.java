package com.apps.dashboard;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.apps.dashboard")
public class ApplicationsDashboard implements CommandLineRunner {

  @Autowired
  private ApplicationService applicationService;

  public static void main(String[] args) {
    SpringApplication.run(ApplicationsDashboard.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    final Long appId = 1234L;

    Application application = Application
        .builder()
        .id(1234L)
        .name("Test")
        .dns("Http://localhost:8080")
        .healthEndpoint("/test")
        .build();

    applicationService.createApplication(application);

    System.out.println(applicationService.getApplicationById(appId).getHealthCheckUrl());

  }
}
