package com.apps.dashboard.controllers.model.mapper;

import com.apps.dashboard.controllers.model.ApplicationModel;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationStatusService;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class ModelMapperTest {

  @InjectMocks
  private ModelMapper modelMapper;

  @Mock
  private ApplicationStatusService applicationStatusService;

  /**
   * Test convertApplication
   */
  @Test
  public void testConvertApplication() {
    final Long id = 123L;
    final String applicationName = "appName";
    final String dns = "https:localhost.com";
    final String healthCheckEndpoint = "/ping";

    final ApplicationModel applicationModel = new ApplicationModel();
    applicationModel.setId(id);
    applicationModel.setName(applicationName);
    applicationModel.setDns(dns);
    applicationModel.setHealthEndpoint(healthCheckEndpoint);

    Application app = modelMapper.convertApplication(applicationModel);

    Assertions.assertEquals(id, app.getId());
    Assertions.assertEquals(applicationName, app.getName());
    Assertions.assertEquals(dns, app.getDns());
    Assertions.assertEquals(healthCheckEndpoint, app.getHealthEndpoint());
  }

}