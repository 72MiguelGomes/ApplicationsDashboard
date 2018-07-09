package com.apps.dashboard.services;

import com.apps.dashboard.model.Application;
import java.util.Collection;
import javax.annotation.Nonnull;

public interface ApplicationService {

  Application createApplication(@Nonnull Application application);

  @Nonnull
  Collection<Application> getAllApplications();

  @Nonnull
  Application getApplicationById(@Nonnull Long id);

  @Nonnull
  Application updateApplication(@Nonnull Long id, @Nonnull Application application);

}
