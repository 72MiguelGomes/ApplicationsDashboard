package com.apps.dashboard.config;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public Client client() {
    return ClientBuilder.newClient();
  }

}
