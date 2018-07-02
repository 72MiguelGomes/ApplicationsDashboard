package com.apps.dashboard.updater.requests;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import javax.annotation.Nonnull;

public interface ApplicationMonitor {

  @Nonnull
  ServiceInfo checkApplicationStatus(@Nonnull Application application);

}
