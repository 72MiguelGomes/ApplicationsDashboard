package com.apps.dashboard.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ServiceInfoTest {

  @Test
  void testUpdate() {
    final Long applicationId = 123L;
    final boolean healthy = false;
    final String version = "v0";

    ServiceInfo newServiceInfo = ServiceInfo.builder()
        .applicationId(321L)
        .healthy(healthy)
        .version(version)
        .build();

    ServiceInfo oldServiceInfo = ServiceInfo.builder()
        .applicationId(applicationId)
        .healthy(true)
        .version("v2")
        .build();

    ServiceInfo updatedService = oldServiceInfo.update(newServiceInfo);

    assertEquals(applicationId, updatedService.getApplicationId());
    assertEquals(healthy, updatedService.isHealthy());
    assertEquals(version, updatedService.getVersion());

  }

}