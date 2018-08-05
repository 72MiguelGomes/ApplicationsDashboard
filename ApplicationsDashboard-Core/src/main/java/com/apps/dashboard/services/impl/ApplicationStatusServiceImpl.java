package com.apps.dashboard.services.impl;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.EndpointInfo;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import com.apps.dashboard.services.ApplicationStatusService;
import com.google.common.base.Preconditions;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationStatusServiceImpl implements ApplicationStatusService {

  private ApplicationStatusRepo applicationStatusRepo;

  private RequestHelper requestHelper;

  @Autowired
  public ApplicationStatusServiceImpl(
      ApplicationStatusRepo applicationStatusRepo,
      RequestHelper requestHelper) {
    this.applicationStatusRepo = applicationStatusRepo;
    this.requestHelper = requestHelper;
  }

  @Override
  public void updateApplicationStatus(@Nonnull Long applicationId,
      @Nonnull ServiceInfo serviceInfo) {

    ServiceInfo newServiceInfo = getApplicationStatus(applicationId)
        .orElse(ServiceInfo.empty(applicationId))
        .update(serviceInfo);

    applicationStatusRepo.updateApplicationStatus(newServiceInfo);
  }

  @Override
  public Optional<ServiceInfo> getApplicationStatus(@Nonnull Long applicationId) {
    Preconditions.checkArgument(Objects.nonNull(applicationId), "ApplicationId must non null");

    return Optional.ofNullable(applicationStatusRepo.getApplicationStatus(applicationId));
  }

  @Override
  @Nonnull
  public Collection<EndpointInfo> getEndpointsInfo(@Nonnull Application application) {

    Optional<ServiceInfo> serviceInfoOpt = getApplicationStatus(application.getId());

    final Collection<EndpointInfo> endpointInfo = new ArrayList<>();

    serviceInfoOpt.ifPresent(serviceInfo -> {
      serviceInfo.getInfoEndpoints()
          .forEach(endpoint -> {
            String url = MessageFormat.format("{0}{1}",
                application.getDns(),
                endpoint);

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
