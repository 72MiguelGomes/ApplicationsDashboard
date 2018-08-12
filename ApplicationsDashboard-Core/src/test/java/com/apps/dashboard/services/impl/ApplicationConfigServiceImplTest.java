package com.apps.dashboard.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.external.requests.RequestHelper;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.model.ApplicationConfig;
import com.apps.dashboard.model.EndpointInfo;
import com.apps.dashboard.repositories.ApplicationConfigRepo;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import javax.xml.ws.Response;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigServiceImplTest {

  @InjectMocks
  private ApplicationConfigServiceImpl applicationConfigService;

  @Mock
  private ApplicationConfigRepo applicationConfigRepo;

  @Mock
  private RequestHelper requestHelper;

  /**
   * Test getApplicationConfigById
   */
  @Test
  void testGetApplicationConfigById() {
    final Long appId = 123L;

    Set<String> infoEndpoints = Sets.newHashSet("/ping");

    final ApplicationConfig mockApplicationConfig = ApplicationConfig.builder()
        .applicationId(appId)
        .infoEndpoints(infoEndpoints)
        .build();

    when(this.applicationConfigRepo.getApplicationConfigById(eq(appId)))
        .thenReturn(Optional.of(mockApplicationConfig));

    final Optional<ApplicationConfig> applicationConfigOpt = this.applicationConfigService
        .getApplicationConfigById(appId);

    Assertions.assertTrue(applicationConfigOpt.isPresent());

    final ApplicationConfig applicationConfig = applicationConfigOpt.get();

    assertEquals(appId, applicationConfig.getApplicationId());
    assertEquals(infoEndpoints, applicationConfig.getInfoEndpoints());
  }

  @Test
  void testCreateApplicationConfig() {
    final Long appId = 123L;

    Set<String> infoEndpoints = Sets.newHashSet("/ping");

    final ApplicationConfig applicationConfig = ApplicationConfig.builder()
        .applicationId(appId)
        .infoEndpoints(infoEndpoints)
        .build();

    when(this.applicationConfigRepo.saveOrUpdate(any(ApplicationConfig.class)))
        .thenReturn(applicationConfig);

    ArgumentCaptor<ApplicationConfig> applicationConfigArgumentCaptor = ArgumentCaptor
        .forClass(ApplicationConfig.class);

    ApplicationConfig appConfig = this.applicationConfigService
        .createApplicationConfig(applicationConfig);

    verify(this.applicationConfigRepo, times(1))
        .saveOrUpdate(applicationConfigArgumentCaptor.capture());

    ApplicationConfig appConfigPassed = applicationConfigArgumentCaptor.getValue();

    assertEquals(appId, appConfigPassed.getApplicationId());
    assertEquals(infoEndpoints, appConfigPassed.getInfoEndpoints());

    assertEquals(appId, appConfig.getApplicationId());
    assertEquals(infoEndpoints, appConfig.getInfoEndpoints());
  }

  @Test
  void testUpdateApplicationConfig() {
    final Long appId = 123L;
    final Long difAppId = 321L;

    Set<String> infoEndpoints = Sets.newHashSet("/ping");

    final ApplicationConfig applicationConfig = ApplicationConfig.builder()
        .applicationId(difAppId)
        .infoEndpoints(infoEndpoints)
        .build();

    when(this.applicationConfigRepo.saveOrUpdate(any(ApplicationConfig.class)))
        .thenReturn(ApplicationConfig.builder()
            .applicationId(appId)
            .infoEndpoints(infoEndpoints)
            .build());

    ApplicationConfig updatedApplicationConfig = this.applicationConfigService
        .updateApplicationConfig(appId, applicationConfig);

    ArgumentCaptor<ApplicationConfig> applicationConfigArgumentCaptor = ArgumentCaptor
        .forClass(ApplicationConfig.class);

    verify(this.applicationConfigRepo, times(1))
        .saveOrUpdate(applicationConfigArgumentCaptor.capture());

    ApplicationConfig passedApplicationConfig = applicationConfigArgumentCaptor.getValue();

    assertEquals(appId, updatedApplicationConfig.getApplicationId());
    assertEquals(infoEndpoints, updatedApplicationConfig.getInfoEndpoints());

    assertEquals(appId, passedApplicationConfig.getApplicationId());
    assertEquals(infoEndpoints, passedApplicationConfig.getInfoEndpoints());
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

    when(applicationConfigRepo.getApplicationConfigById(eq(appId)))
        .thenReturn(Optional.empty());

    Collection<EndpointInfo> endpointInfos = this.applicationConfigService
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

    final ApplicationConfig serviceConfig = mock(ApplicationConfig.class);
    when(serviceConfig.getInfoEndpoints())
        .thenReturn(Collections.emptySet());

    when(applicationConfigRepo.getApplicationConfigById(eq(appId)))
        .thenReturn(Optional.of(serviceConfig));

    Collection<EndpointInfo> endpointInfos = this.applicationConfigService
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

    final ApplicationConfig serviceConfig = mock(ApplicationConfig.class);
    when(serviceConfig.getInfoEndpoints())
        .thenReturn(Sets.newHashSet(endpoint));

    when(applicationConfigRepo.getApplicationConfigById(eq(appId)))
        .thenReturn(Optional.of(serviceConfig));

    when(requestHelper.performGetRequest(Mockito.anyString()))
        .thenAnswer((Answer<Response>) invocationOnMock -> {
          throw new RuntimeException(exceptionMessage);
        });

    Collection<EndpointInfo> endpointInfos = this.applicationConfigService
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

    final javax.ws.rs.core.Response response = Mockito.mock(javax.ws.rs.core.Response.class);
    when(response.readEntity(String.class))
        .thenReturn(message);

    final ApplicationConfig serviceConfig = mock(ApplicationConfig.class);
    when(serviceConfig.getInfoEndpoints())
        .thenReturn(Sets.newHashSet(endpoint));

    when(applicationConfigRepo.getApplicationConfigById(eq(appId)))
        .thenReturn(Optional.of(serviceConfig));

    when(requestHelper.performGetRequest(Mockito.anyString()))
        .thenReturn(response);

    Collection<EndpointInfo> endpointInfos = this.applicationConfigService
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