package com.apps.dashboard.services;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ApplicationConfig;
import com.apps.dashboard.model.EndpointInfo;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface ApplicationConfigService {

  Optional<ApplicationConfig> getApplicationConfigById(@Nonnull Long appId);

  @Nonnull
  ApplicationConfig createApplicationConfig(@Nonnull ApplicationConfig applicationConfig);

  @Nonnull
  ApplicationConfig updateApplicationConfig(@Nonnull Long appId, @Nonnull ApplicationConfig applicationConfig);

  Collection<EndpointInfo> getEndpointsInfo(@Nonnull Application application);
}
