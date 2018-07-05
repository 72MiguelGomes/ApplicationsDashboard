package com.apps.dashboard.services;

import com.apps.dashboard.model.Application;
import javax.annotation.Nonnull;

public interface ApplicationService {

  void createApplication(@Nonnull Application application);

  @Nonnull
  Application getApplicationById(@Nonnull Long id);

  @Nonnull
  Application updateApplication(@Nonnull Long id, @Nonnull Application application);

}
