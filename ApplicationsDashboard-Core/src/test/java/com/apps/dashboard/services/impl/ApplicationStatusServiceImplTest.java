package com.apps.dashboard.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.EndpointInfo;
import com.apps.dashboard.model.ServiceInfo;
import com.apps.dashboard.repositories.ApplicationStatusRepo;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import javax.xml.ws.Response;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
public class ApplicationStatusServiceImplTest {

  @InjectMocks
  private ApplicationStatusServiceImpl applicationStatusService;

  @Mock
  private ApplicationStatusRepo applicationStatusRepo;

  @Mock
  private RequestHelper requestHelper;

  /**
   * Test updateApplicationStatus
   */
  @Test
  public void updateApplicationStatus() {

    final Long appId = 1234L;
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

    ArgumentCaptor<ServiceInfo> serviceInfoArgumentCaptor = ArgumentCaptor
        .forClass(ServiceInfo.class);

    verify(this.applicationStatusRepo).updateApplicationStatus(serviceInfoArgumentCaptor.capture());

    ServiceInfo updatedServiceInfo = serviceInfoArgumentCaptor.getValue();

    assertEquals(appId, updatedServiceInfo.getApplicationId());
    assertEquals(appVersion, updatedServiceInfo.getVersion());
    assertEquals(health, updatedServiceInfo.isHealthy());
  }

  @Test
  public void updateApplicationStatusWithNullValue() {
    final Long applicationID = null;

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId(1234L)
        .version("v1")
        .healthy(false)
        .build();

    assertThrows(IllegalArgumentException.class, () -> {
      applicationStatusService.updateApplicationStatus(applicationID, serviceInfo);
    });
  }

  @Test
  public void updateApplicationStatusWithNewAppId() {
    final Long applicationID = 100000L;
    final String version = "v1";
    final boolean healthy = false;

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId(applicationID)
        .version(version)
        .healthy(healthy)
        .build();

    applicationStatusService.updateApplicationStatus(applicationID, serviceInfo);

    ArgumentCaptor<ServiceInfo> serviceInfoArgumentCaptor = ArgumentCaptor
        .forClass(ServiceInfo.class);

    verify(this.applicationStatusRepo).updateApplicationStatus(serviceInfoArgumentCaptor.capture());

    ServiceInfo updatedServiceInfo = serviceInfoArgumentCaptor.getValue();

    assertEquals(applicationID, updatedServiceInfo.getApplicationId());
    assertEquals(version, updatedServiceInfo.getVersion());
    assertEquals(healthy, updatedServiceInfo.isHealthy());
  }

  /**
   * Test getApplicationStatus
   */
  @Test
  public void testGetApplicationStatus() {

    final Long applicationID = 123L;
    final String appVersion = "v1";
    final boolean health = true;

    final ServiceInfo serviceInfo = ServiceInfo.builder()
        .applicationId(applicationID)
        .version(appVersion)
        .healthy(health)
        .build();

    when(this.applicationStatusRepo.getApplicationStatus(eq(applicationID)))
        .thenReturn(serviceInfo);

    Optional<ServiceInfo> serviceInfoOpt = this.applicationStatusService
        .getApplicationStatus(applicationID);

    assertTrue(serviceInfoOpt.isPresent());

    serviceInfoOpt.ifPresent(newServiceInfo -> {
      assertEquals(applicationID, newServiceInfo.getApplicationId());
      assertEquals(appVersion, newServiceInfo.getVersion());
      assertEquals(health, newServiceInfo.isHealthy());
    });
  }

  @Test
  public void testGetApplicationStatusWithNullId() {
    final Long applicationID = null;

    assertThrows(IllegalArgumentException.class, () -> {
      applicationStatusService.getApplicationStatus(applicationID);
    });
  }

  @Test
  public void testGetApplicationStatusWithNonExistentId() {

    final Long applicationID = 100000L;

    when(this.applicationStatusRepo.getApplicationStatus(eq(applicationID)))
        .thenReturn(null);

    Optional<ServiceInfo> serviceInfo = this.applicationStatusService
        .getApplicationStatus(applicationID);

    assertFalse(serviceInfo.isPresent());
  }

  /**
   * Test getEndpointsInfo
   */
  @Test
  void testGetEndpointsInfoNonExistentServiceInfo() {
    final String dns = "http://localhost:8080";
    final Long appId = 123L;

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .build();

    when(applicationStatusRepo.getApplicationStatus(eq(appId)))
        .thenReturn(null);

    Collection<EndpointInfo> endpointInfos = this.applicationStatusService
        .getEndpointsInfo(application);

    assertEquals(0, endpointInfos.size());

    verify(this.requestHelper, never()).performGetRequest(anyString());
  }

  @Test
  public void testGetEndpointsInfoNonEmptyEndpointList() {
    final String dns = "http://localhost:8080";
    final Long appId = 123L;

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .build();

    final ServiceInfo serviceInfo = mock(ServiceInfo.class);
    when(serviceInfo.getInfoEndpoints())
        .thenReturn(Collections.emptySet());

    when(applicationStatusRepo.getApplicationStatus(eq(appId)))
        .thenReturn(serviceInfo);

    Collection<EndpointInfo> endpointInfos = this.applicationStatusService
        .getEndpointsInfo(application);

    assertEquals(0, endpointInfos.size());

    verify(this.requestHelper, never()).performGetRequest(anyString());
  }

  @Test
  public void testGetEndpointsInfoExceptionPerformingRequest() {
    final String dns = "http://localhost:8080";
    final Long appId = 123L;
    final String exceptionMessage = "Request Timeout";
    final String endpoint = "/ping";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .build();

    final ServiceInfo serviceInfo = mock(ServiceInfo.class);
    when(serviceInfo.getInfoEndpoints())
        .thenReturn(Sets.newHashSet(endpoint));

    when(applicationStatusRepo.getApplicationStatus(eq(appId)))
        .thenReturn(serviceInfo);

    when(requestHelper.performGetRequest(Mockito.anyString()))
        .thenAnswer((Answer<Response>) invocationOnMock -> {
          throw new RuntimeException(exceptionMessage);
        });

    Collection<EndpointInfo> endpointInfos = this.applicationStatusService
        .getEndpointsInfo(application);

    assertEquals(1, endpointInfos.size());

    verify(this.requestHelper, atLeastOnce()).performGetRequest(anyString());

    EndpointInfo endpointInfo = endpointInfos.stream()
        .findFirst()
        .orElseThrow(IllegalStateException::new);

    assertEquals(endpoint, endpointInfo.getEndpoint());
    assertEquals(exceptionMessage, endpointInfo.getResult());
  }

  @Test
  public void testGetEndpointsInfo() {
    final String dns = "http://localhost:8080";
    final Long appId = 123L;
    final String message = "works";
    final String endpoint = "/ping";

    final Application application = Application.builder()
        .id(appId)
        .dns(dns)
        .build();

    final ServiceInfo serviceInfo = mock(ServiceInfo.class);
    when(serviceInfo.getInfoEndpoints())
        .thenReturn(Sets.newHashSet(endpoint));

    final javax.ws.rs.core.Response response = Mockito.mock(javax.ws.rs.core.Response.class);
    when(response.readEntity(String.class))
        .thenReturn(message);

    when(applicationStatusRepo.getApplicationStatus(eq(appId)))
        .thenReturn(serviceInfo);

    when(requestHelper.performGetRequest(Mockito.anyString()))
        .thenReturn(response);

    Collection<EndpointInfo> endpointInfos = this.applicationStatusService
        .getEndpointsInfo(application);

    assertEquals(1, endpointInfos.size());

    verify(this.requestHelper, atLeastOnce()).performGetRequest(anyString());

    EndpointInfo endpointInfo = endpointInfos.stream()
        .findFirst()
        .orElseThrow(IllegalStateException::new);

    assertEquals(endpoint, endpointInfo.getEndpoint());
    assertEquals(message, endpointInfo.getResult());
  }

}
