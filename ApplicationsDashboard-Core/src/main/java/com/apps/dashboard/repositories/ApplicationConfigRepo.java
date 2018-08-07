package com.apps.dashboard.repositories;

import com.apps.dashboard.model.ApplicationConfig;
import java.util.Optional;
import javax.annotation.Nonnull;

public interface ApplicationConfigRepo {

  @Nonnull
  ApplicationConfig saveOrUpdate(@Nonnull ApplicationConfig applicationConfig);

  Optional<ApplicationConfig> getApplicationConfigById(@Nonnull Long id);

}
