package com.apps.dashboard.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.exceptions.EntityNotFoundException;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class ApplicationStatusServiceImplTest {

  private ApplicationStatusRepo applicationStatusRepo;

  private ApplicationStatusServiceImpl applicationStatusService;

  public ApplicationStatusServiceImplTest() {
    this.applicationStatusRepo = mock(ApplicationStatusRepo.class);
    this.applicationStatusService = new ApplicationStatusServiceImpl(this.applicationStatusRepo);
  }

  /**
   * Test updateApplicationStatus
   */
  @Test
  public void updateApplicationStatus() {

    final String appId = "1234";
    final String appVersion = "v2";
    final boolean health = true;

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId(appId)
        .version("v1")
        .healthy(false)
        .build();

    when(this.applicationStatusRepo.getApplicationStatus(eq(appId)))
        .thenReturn(serviceInfo);


    final ServiceInfo newServiceInfo = ServiceInfo.builder()
        .version(appVersion)
        .healthy(health)
        .build();

    this.applicationStatusService.updateApplicationStatus(appId, newServiceInfo);

    ArgumentCaptor<ServiceInfo> serviceInfoArgumentCaptor = ArgumentCaptor.forClass(ServiceInfo.class);

    verify(this.applicationStatusRepo).updateApplicationStatus(serviceInfoArgumentCaptor.capture());

    ServiceInfo updatedServiceInfo = serviceInfoArgumentCaptor.getValue();

    assertEquals(appId, updatedServiceInfo.getApplicationId());
    assertEquals(appVersion, updatedServiceInfo.getVersion());
    assertEquals(health, updatedServiceInfo.isHealthy());
  }

  @Test
  public void updateApplicationStatusWithNullValue() {
    final String applicationID = StringUtils.EMPTY;

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId("1234")
        .version("v1")
        .healthy(false)
        .build();

    assertThrows(IllegalArgumentException.class, () -> {
      applicationStatusService.updateApplicationStatus(applicationID, serviceInfo);
    });
  }

  @Test
  public void updateApplicationStatusWithInvalidAppId() {
    final String applicationID = "invalidId";

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId("invalidId")
        .version("v1")
        .healthy(false)
        .build();

    assertThrows(EntityNotFoundException.class, () -> {
      applicationStatusService.updateApplicationStatus(applicationID, serviceInfo);
    });
  }

}
