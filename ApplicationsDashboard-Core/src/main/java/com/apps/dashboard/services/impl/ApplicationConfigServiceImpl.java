package com.apps.dashboard.services.impl;

import com.apps.dashboard.model.ApplicationConfig;
import com.apps.dashboard.repositories.ApplicationConfigRepo;
import com.apps.dashboard.services.ApplicationConfigService;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

  private final ApplicationConfigRepo applicationConfigRepo;

  @Autowired
  public ApplicationConfigServiceImpl(
      ApplicationConfigRepo applicationConfigRepo) {
    this.applicationConfigRepo = applicationConfigRepo;
  }

  @Override
  public Optional<ApplicationConfig> getApplicationConfigById(@Nonnull Long appId) {
    return this.applicationConfigRepo.getApplicationConfigById(appId);
  }

  @Nonnull
  @Override
  public ApplicationConfig createApplicationConfig(@Nonnull ApplicationConfig applicationConfig) {
    return this.applicationConfigRepo.saveOrUpdate(applicationConfig);
  }

  @Nonnull
  @Override
  public ApplicationConfig updateApplicationConfig(@Nonnull Long appId,
      @Nonnull ApplicationConfig applicationConfig) {

    ApplicationConfig newAppConfig = ApplicationConfig.empty(appId)
        .update(applicationConfig);

    return this.applicationConfigRepo.saveOrUpdate(newAppConfig);
  }
}
