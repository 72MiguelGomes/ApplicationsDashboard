package com.apps.dashboard.services.impl;

import static org.mockito.ArgumentMatchers.eq;
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
import org.mockito.Mockito;

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

    final Optional<ApplicationConfig> applicationConfigOpt = this.applicationConfigService.getApplicationConfigById(appId);

    Assertions.assertTrue(applicationConfigOpt.isPresent());

    final ApplicationConfig applicationConfig = applicationConfigOpt.get();

    Assertions.assertEquals(appId, applicationConfig.getApplicationId());
    Assertions.assertEquals(infoEndpoints, applicationConfig.getInfoEndpoints());
  }

  @Test
  void testCreateApplicationConfig() {
    final Long appId = 123L;

    Set<String> infoEndpoints = Sets.newHashSet("/ping");

    final ApplicationConfig applicationConfig = ApplicationConfig.builder()
        .applicationId(appId)
        .infoEndpoints(infoEndpoints)
        .build();

    ArgumentCaptor<ApplicationConfig> applicationConfigArgumentCaptor = ArgumentCaptor.forClass(ApplicationConfig.class);

    Mockito.verify(this.applicationConfigRepo, Mockito.times(1))
        .saveOrUpdate(applicationConfigArgumentCaptor.capture());

    ApplicationConfig appConfig = this.applicationConfigService.createApplicationConfig(applicationConfig);

    ApplicationConfig appConfigPassed = applicationConfigArgumentCaptor.getValue();

    Assertions.assertEquals(appId, appConfigPassed.getApplicationId());
    Assertions.assertEquals(infoEndpoints, appConfigPassed.getInfoEndpoints());

    Assertions.assertEquals(appId, appConfig.getApplicationId());
    Assertions.assertEquals(infoEndpoints, appConfig.getInfoEndpoints());
  }

}