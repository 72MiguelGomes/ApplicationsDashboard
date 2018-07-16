package com.apps.dashboard.updater.requests.impl;

import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.updater.requests.ApplicationMonitor;
import javax.annotation.Nonnull;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultApplicationMonitor implements ApplicationMonitor {

  private static Logger LOGGER = LoggerFactory.getLogger(DefaultApplicationMonitor.class);

  private Client client;

  @Autowired
  public DefaultApplicationMonitor(Client client) {
    this.client = client;
  }

  @Nonnull
  @Override
  public ServiceInfo checkApplicationStatus(@Nonnull Application application) {

    LOGGER.debug("Checking status for application {} on url {}",
        application.getId(),
        application.getHealthCheckUrl());

    Invocation.Builder builder = client.target(application.getHealthCheckUrl())
        .request()
        .property(CONNECT_TIMEOUT, 500)
        .property(READ_TIMEOUT, 500);

    boolean healthy = false;

    try {
      Response response = builder.get();

      healthy = response.getStatusInfo().getFamily() == Family.SUCCESSFUL;
    } catch (ProcessingException e) {
      LOGGER.warn("Possible Timeout", e.getCause());
    }

    LOGGER.debug("Healthy: {}", healthy);

    return ServiceInfo.builder()
        .healthy(healthy)
        .applicationId(application.getId())
        .build();
  }
}
