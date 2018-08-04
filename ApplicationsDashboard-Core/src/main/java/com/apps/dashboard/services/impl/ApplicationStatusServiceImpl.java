package com.apps.dashboard.services.impl;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import com.apps.dashboard.services.ApplicationStatusService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.text.MessageFormat;
import java.util.Map;
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
  public Map<String, String> getEndpointsInfo(@Nonnull Application application) {

    Optional<ServiceInfo> serviceInfoOpt = getApplicationStatus(application.getId());

    final Map<String, String> endpointInfo = Maps.newHashMap();

    serviceInfoOpt.ifPresent(serviceInfo -> {
      serviceInfo.getInfoEndpoints()
          .forEach(endpoint -> {
            String url = MessageFormat.format("{0}{1}",
                application.getDns(),
                endpoint);

            try {
              Response response = requestHelper.performGetRequest(url);

              String bodyResponse = response.readEntity(String.class);

              endpointInfo.put(endpoint, bodyResponse);
            } catch (Exception e) {
              endpointInfo.put(endpoint, e.getMessage());
            }
          });
    });

    return endpointInfo;
  }
}
