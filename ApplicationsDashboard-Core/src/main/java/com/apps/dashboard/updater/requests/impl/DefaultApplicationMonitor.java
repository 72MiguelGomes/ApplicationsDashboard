package com.apps.dashboard.updater.requests.impl;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.updater.requests.ApplicationMonitor;
import javax.annotation.Nonnull;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultApplicationMonitor implements ApplicationMonitor {

  private static Logger LOGGER = LoggerFactory.getLogger(DefaultApplicationMonitor.class);

  private RequestHelper requestHelper;

  @Autowired
  public DefaultApplicationMonitor(RequestHelper requestHelper) {
    this.requestHelper = requestHelper;
  }

  @Nonnull
  @Override
  public ServiceInfo checkApplicationStatus(@Nonnull Application application) {

    LOGGER.debug("Checking status for application {} on url {}",
        application.getId(),
        application.getHealthCheckUrl());

    boolean healthy = false;

    try {
      Response response = requestHelper.performGetRequest(application.getHealthCheckUrl());

      healthy = response.getStatusInfo().getFamily() == Family.SUCCESSFUL;
    } catch (ProcessingException e) {
      LOGGER.warn(e.getMessage());
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    LOGGER.debug("Healthy: {}", healthy);

    return ServiceInfo.builder()
        .healthy(healthy)
        .applicationId(application.getId())
        .build();
  }
}
