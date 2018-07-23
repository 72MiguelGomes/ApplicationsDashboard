package com.apps.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.apps.dashboard")
public class ApplicationsDashboardWeb {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationsDashboardWeb.class, args);
  }

}
