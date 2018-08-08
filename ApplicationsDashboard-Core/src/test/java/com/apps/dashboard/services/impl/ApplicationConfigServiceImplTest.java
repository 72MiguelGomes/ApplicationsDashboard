package com.apps.dashboard.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apps.dashboard.model.ApplicationConfig;
import com.apps.dashboard.repositories.ApplicationConfigRepo;
import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigServiceImplTest {

  @InjectMocks
  private ApplicationConfigServiceImpl applicationConfigService;

  @Mock
  private ApplicationConfigRepo applicationConfigRepo;

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

}