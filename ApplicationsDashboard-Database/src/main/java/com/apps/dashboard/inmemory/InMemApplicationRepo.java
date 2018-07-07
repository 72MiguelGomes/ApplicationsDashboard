package com.apps.dashboard.inmemory;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.repositories.ApplicationRepo;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class InMemApplicationRepo implements ApplicationRepo {

  private Map<Long, Application> applicationMap = new HashMap<>();

  private volatile AtomicLong nextAppId = new AtomicLong(1);

  @Override
  public Application saveOrUpdate(Application application) {

    Application savedApp = application;

    if (application.getId() == null) {
      Long appId = nextAppId.getAndIncrement();

      savedApp = application.update()
          .id(appId)
          .build();
    }

    applicationMap.put(savedApp.getId(), savedApp);

    return savedApp;
  }

  @Override
  public Optional<Application> getApplicationById(Long id) {
    return Optional.ofNullable(applicationMap.get(id));
  }

  @Override
  public Collection<Application> getAllApplications() {
    return this.applicationMap.values();
  }
}
