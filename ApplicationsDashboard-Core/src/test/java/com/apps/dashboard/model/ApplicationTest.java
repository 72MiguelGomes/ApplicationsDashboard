package com.apps.dashboard.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void testUpdateApplication() {
    final Long appId = 123L;
    final String name = "name";
    final String dns = "dns";
    final String healthCheckEndpoint = "healthCheckEndpoint";

    final Application oldApplication = Application.builder()
        .id(appId)
        .name(mixString(name))
        .dns(mixString(dns))
        .healthEndpoint(mixString(healthCheckEndpoint))
        .build();

    final Application newApplication = Application.builder()
        .id(321L) // This should be ignored during the update
        .name(name)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final Application updatedApp = oldApplication.update(newApplication);

    Assertions.assertEquals(appId, updatedApp.getId());
    Assertions.assertEquals(name, updatedApp.getName());
    Assertions.assertEquals(dns, updatedApp.getDns());
    Assertions.assertEquals(healthCheckEndpoint, updatedApp.getHealthEndpoint());
  }

  @Test
  void testUpdateCopy() {
    final Long appId = 123L;
    final String name = "name";
    final String dns = "dns";
    final String healthCheckEndpoint = "healthCheckEndpoint";

    final Application oldApplication = Application.builder()
        .id(appId)
        .name(name)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final Application newApplication = oldApplication.update().build();

    Assertions.assertEquals(appId, newApplication.getId());
    Assertions.assertEquals(name, newApplication.getName());
    Assertions.assertEquals(dns, newApplication.getDns());
    Assertions.assertEquals(healthCheckEndpoint, newApplication.getHealthEndpoint());
  }

  @Test
  void testUpdate() {

    final Long appId = 123L;
    final String name = "name";
    final String dns = "dns";
    final String healthCheckEndpoint = "healthCheckEndpoint";

    final Application oldApplication = Application.builder()
        .id(appId)
        .name("asd")
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final Application newApplication = oldApplication.update()
        .name(name)
        .build();

    Assertions.assertEquals(appId, newApplication.getId());
    Assertions.assertEquals(name, newApplication.getName());
    Assertions.assertEquals(dns, newApplication.getDns());
    Assertions.assertEquals(healthCheckEndpoint, newApplication.getHealthEndpoint());
  }

  private String mixString(String value) {
    return value + "123";
  }

}