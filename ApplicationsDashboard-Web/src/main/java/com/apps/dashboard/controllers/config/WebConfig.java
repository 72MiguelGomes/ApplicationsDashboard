package com.apps.dashboard.controllers.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

  @Bean
  LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }

}
