package com.apps.dashboard.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationConfigTest {

  @Test
  void testUpdate() {
    final Long applicationId = 123L;
    final Set<String> infoEndpoints = Sets.newHashSet(
        "/config"
    );

    ApplicationConfig newApplicationConfig = ApplicationConfig.builder()
        .applicationId(321L)
        .infoEndpoints(infoEndpoints)
        .build();

    ApplicationConfig oldApplicationConfig = ApplicationConfig.builder()
        .applicationId(applicationId)
        .infoEndpoints(Sets.newHashSet(
            "/oldConfig"
        ))
        .build();

    ApplicationConfig updatedService = oldApplicationConfig.update(newApplicationConfig);

    assertEquals(applicationId, updatedService.getApplicationId());
    assertEquals(infoEndpoints, updatedService.getInfoEndpoints());

    // Test immutability
    assertEquals(1, newApplicationConfig.getInfoEndpoints().size());
    assertEquals(1, updatedService.getInfoEndpoints().size());
    newApplicationConfig.getInfoEndpoints().add("/test");
    assertEquals(2, newApplicationConfig.getInfoEndpoints().size());
    assertEquals(1, updatedService.getInfoEndpoints().size());
  }

  @Test
  void testEmpty() {

    final Long applicationId = 123L;

    ApplicationConfig applicationConfig = ApplicationConfig
        .empty(applicationId);

    Assertions.assertEquals(applicationId, applicationConfig.getApplicationId());
    Assertions.assertNull(applicationConfig.getInfoEndpoints());
  }

}