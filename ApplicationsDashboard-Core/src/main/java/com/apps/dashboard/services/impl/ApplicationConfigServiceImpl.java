package com.apps.dashboard.services.impl;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ApplicationConfig;
import com.apps.dashboard.model.EndpointInfo;
import com.apps.dashboard.repositories.ApplicationConfigRepo;
import com.apps.dashboard.services.ApplicationConfigService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

  private final ApplicationConfigRepo applicationConfigRepo;

  private RequestHelper requestHelper;

  @Autowired
  public ApplicationConfigServiceImpl(
      ApplicationConfigRepo applicationConfigRepo,
      RequestHelper requestHelper) {
    this.applicationConfigRepo = applicationConfigRepo;
    this.requestHelper = requestHelper;
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

  @Override
  @Nonnull
  public Collection<EndpointInfo> getEndpointsInfo(@Nonnull Application application) {

    Optional<ApplicationConfig> applicationConfigOpt = getApplicationConfigById(application.getId());

    final Collection<EndpointInfo> endpointInfo = new ArrayList<>();

    applicationConfigOpt.ifPresent(serviceInfo -> {
      serviceInfo.getInfoEndpoints()
          .forEach(endpoint -> {
            String url = application.createUrl(endpoint);

            String result;

            try {
              Response response = requestHelper.performGetRequest(url);

              result = response.readEntity(String.class);
            } catch (Exception e) {
              result = e.getMessage();
            }

            endpointInfo.add(EndpointInfo.builder()
                .endpoint(endpoint)
                .result(result)
                .build());
          });
    });

    return endpointInfo;
  }
}
