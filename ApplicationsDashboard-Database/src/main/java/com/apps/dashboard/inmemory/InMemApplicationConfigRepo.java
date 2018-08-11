package com.apps.dashboard.inmemory;

import com.apps.dashboard.model.ApplicationConfig;
import com.apps.dashboard.repositories.ApplicationConfigRepo;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class InMemApplicationConfigRepo implements ApplicationConfigRepo {

  private Map<Long, ApplicationConfig> applicationConfigMap = new HashMap<>();

  @Nonnull
  @Override
  public ApplicationConfig saveOrUpdate(@Nonnull ApplicationConfig applicationConfig) {
    return applicationConfigMap.put(applicationConfig.getApplicationId(), applicationConfig);
  }

  @Override
  public Optional<ApplicationConfig> getApplicationConfigById(@Nonnull Long appId) {
    return Optional.ofNullable(applicationConfigMap.get(appId));
  }
}
