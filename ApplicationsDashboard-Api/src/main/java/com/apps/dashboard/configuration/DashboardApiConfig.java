package com.apps.dashboard.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardApiConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

}