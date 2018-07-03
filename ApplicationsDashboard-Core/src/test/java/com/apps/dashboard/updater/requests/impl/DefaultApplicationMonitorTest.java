package com.apps.dashboard.updater.requests.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ServiceInfo;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
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
  private Client client;

  /**
   * Test checkApplicationStatus
   */

  @Test
  public void testCheckApplicationStatus() {

    final String appId = "appId";
    final String dns = "https://localhost:8072";
    final String healthCheckEndpoint = "/test";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final Response response = Response.ok().build();

    final String url = dns + healthCheckEndpoint;

    mockClientGetResponse(url, response);

    ServiceInfo serviceInfo = defaultApplicationMonitor.checkApplicationStatus(application);

    assertTrue(serviceInfo.isHealthy());
    assertEquals(appId, serviceInfo.getApplicationId());
  }

  @Test
  public void testCheckApplicationStatusWithTimeout() {
    final String appId = "appId";
    final String dns = "https://localhost:8072";
    final String healthCheckEndpoint = "/test";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final String url = dns + healthCheckEndpoint;

    Builder builder = createClientMock(url);

    when(builder.get()).thenThrow(ProcessingException.class);

    ServiceInfo serviceInfo = defaultApplicationMonitor.checkApplicationStatus(application);

    assertFalse(serviceInfo.isHealthy());
    assertEquals(appId, serviceInfo.getApplicationId());
  }

  @Test
  public void testCheckApplicationStatusWith503() {
    final String appId = "appId";
    final String dns = "https://localhost:8072";
    final String healthCheckEndpoint = "/test";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .healthEndpoint(healthCheckEndpoint)
        .build();

    final String url = dns + healthCheckEndpoint;

    final Response response503 = Response.status(Status.BAD_GATEWAY).build();

    mockClientGetResponse(url, response503);

    ServiceInfo serviceInfo = defaultApplicationMonitor.checkApplicationStatus(application);

    assertFalse(serviceInfo.isHealthy());
    assertEquals(appId, serviceInfo.getApplicationId());
  }

  private void mockClientGetResponse(String url, Response response) {
    Builder builder = createClientMock(url);

    when(builder.get()).thenReturn(response);
  }

  private Invocation.Builder createClientMock(String url) {
    final WebTarget webTarget = mock(WebTarget.class);

    final Builder builder = mock(Builder.class);
    when(webTarget.request()).thenReturn(builder);

    Invocation.Builder invocationBuilder = mock(Invocation.Builder.class);

    when(invocationBuilder.property(anyString(), any(Object.class))).thenReturn(invocationBuilder);
    when(builder.property(anyString(), any(Object.class))).thenReturn(invocationBuilder);

    when(client.target(eq(url))).thenReturn(webTarget);

    return invocationBuilder;
  }

}
