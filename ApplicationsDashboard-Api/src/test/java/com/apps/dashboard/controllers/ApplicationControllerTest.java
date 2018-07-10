package com.apps.dashboard.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import com.apps.dashboard.api.model.ApplicationCreate;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ApplicationService applicationService;

  @TestConfiguration
  static class EmployeeServiceImplTestContextConfiguration {

    @Bean
    public ModelMapper modelMapper() {
      return new ModelMapper();
    }
  }

  @Test
  public void testCreateApplication() throws Exception {

    final String name = "AppName";
    final String dns = "http://localhost";
    final String healthCheck = "/ping";
    final Long appId = 123L;

    when(this.applicationService
        .createApplication(any(com.apps.dashboard.model.Application.class)))
        .then((Answer<Application>) invocationOnMock -> {

          Application application = invocationOnMock.getArgument(0);

          assertEquals(name, application.getName());
          assertEquals(dns, application.getDns());
          assertEquals(healthCheck, application.getHealthEndpoint());
          assertNull(application.getId());

          return application.update()
              .id(appId)
              .build();
        });

    ApplicationCreate applicationCreate = new ApplicationCreate();
    applicationCreate.setName(name);
    applicationCreate.setDns(dns);
    applicationCreate.setHealthEndpoint(healthCheck);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/application")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new Gson().toJson(applicationCreate)))
        .andExpect(request().asyncStarted())
        .andReturn();

    this.mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(mvcResult))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header()
            .string("Location", "http://localhost/application/" + appId));
  }

}
