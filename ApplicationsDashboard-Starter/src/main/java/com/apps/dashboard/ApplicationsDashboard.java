package com.apps.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.apps.dashboard")
public class ApplicationsDashboard {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationsDashboard.class, args);
  }
}
