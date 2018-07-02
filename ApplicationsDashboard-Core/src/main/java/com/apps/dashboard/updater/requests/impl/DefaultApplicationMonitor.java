package com.apps.dashboard.updater.requests.impl;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.updater.requests.ApplicationMonitor;
import javax.annotation.Nonnull;

public class DefaultApplicationMonitor implements ApplicationMonitor {

  @Nonnull
  @Override
  public ServiceInfo checkApplicationStatus(@Nonnull Application application) {
    return null;
  }
}
