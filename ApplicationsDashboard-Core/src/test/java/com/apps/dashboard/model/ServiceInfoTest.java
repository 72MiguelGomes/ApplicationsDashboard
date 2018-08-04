package com.apps.dashboard.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServiceInfoTest {

  @Test
  void testUpdate() {
    final Long applicationId = 123L;
    final boolean healthy = false;
    final String version = "v0";
    final Set<String> infoEndpoints = Sets.newHashSet(
        "/config"
    );

    ServiceInfo newServiceInfo = ServiceInfo.builder()
        .applicationId(321L)
        .healthy(healthy)
        .version(version)
        .infoEndpoints(infoEndpoints)
        .build();

    ServiceInfo oldServiceInfo = ServiceInfo.builder()
        .applicationId(applicationId)
        .healthy(true)
        .version("v2")
        .infoEndpoints(Sets.newHashSet(
            "/oldConfig"
        ))
        .build();

    ServiceInfo updatedService = oldServiceInfo.update(newServiceInfo);

    assertEquals(applicationId, updatedService.getApplicationId());
    assertEquals(healthy, updatedService.isHealthy());
    assertEquals(version, updatedService.getVersion());
    assertEquals(infoEndpoints, updatedService.getInfoEndpoints());

    // Test immutability
    assertEquals(1, newServiceInfo.getInfoEndpoints().size());
    assertEquals(1, updatedService.getInfoEndpoints().size());
    newServiceInfo.getInfoEndpoints().add("/test");
    assertEquals(2, newServiceInfo.getInfoEndpoints().size());
    assertEquals(1, updatedService.getInfoEndpoints().size());

  }

}