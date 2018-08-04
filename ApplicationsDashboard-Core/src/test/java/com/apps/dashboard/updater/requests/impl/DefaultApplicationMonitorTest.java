package com.apps.dashboard.updater.requests.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class DefaultApplicationMonitorTest {

  @InjectMocks
  private DefaultApplicationMonitor defaultApplicationMonitor;

  @Mock
  private RequestHelper requestHelper;

  /**
   * Test checkApplicationStatus
   */

  @Test
  public void testCheckApplicationStatus() {

    final Long appId = 1234L;
    final String dns = "https://localhost:8072";
    final String healthCheckEndpoint = "/test";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final Response response = Response.ok().build();

    final String url = dns + healthCheckEndpoint;

    when(this.requestHelper.performGetRequest(eq(url)))
        .thenReturn(response);

    ServiceInfo serviceInfo = defaultApplicationMonitor.checkApplicationStatus(application);

    assertTrue(serviceInfo.isHealthy());
    assertEquals(appId, serviceInfo.getApplicationId());
  }

  @Test
  public void testCheckApplicationStatusWithTimeout() {
    final Long appId = 1234L;
    final String dns = "https://localhost:8072";
    final String healthCheckEndpoint = "/test";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final String url = dns + healthCheckEndpoint;

    when(this.requestHelper.performGetRequest(eq(url)))
        .thenThrow(ProcessingException.class);

    ServiceInfo serviceInfo = defaultApplicationMonitor.checkApplicationStatus(application);

    assertFalse(serviceInfo.isHealthy());
    assertEquals(appId, serviceInfo.getApplicationId());
  }

  @Test
  public void testCheckApplicationStatusWith503() {
    final Long appId = 1234L;
    final String dns = "https://localhost:8072";
    final String healthCheckEndpoint = "/test";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final String url = dns + healthCheckEndpoint;

    final Response response503 = Response.status(Status.BAD_GATEWAY).build();

    when(this.requestHelper.performGetRequest(eq(url)))
        .thenReturn(response503);

    ServiceInfo serviceInfo = defaultApplicationMonitor.checkApplicationStatus(application);

    assertFalse(serviceInfo.isHealthy());
    assertEquals(appId, serviceInfo.getApplicationId());
  }

}
