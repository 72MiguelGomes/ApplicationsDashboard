package com.apps.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.apps.dashboard")
public class DashboardAppSpring {

  public static void main(String[] args) {
    SpringApplication.run(DashboardAppSpring.class, args);
  }

}
