package com.apps.dashboard.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.apps.dashboard.controllers.config.MockCoreConfig;
import com.apps.dashboard.model.Application;
import com.apps.dashboard.services.ApplicationService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApplicationController.class)
@ContextConfiguration(classes = MockCoreConfig.class)
class ApplicationControllerTest {

  @Autowired
  private WebClient webClient;

  @Autowired
  private ApplicationService applicationService;

  @Test
  public void getApplicationsViewTest() throws Exception {
    HtmlPage page = this.webClient.getPage("/application");

    assertThat(page.getElementsByName("app").size()).isEqualTo(1);
  }

  @Test
  public void navigateToRegistNewApplication() throws Exception {

    HtmlPage page = this.webClient.getPage("/application");

    assertThat(page.getElementsByName("app").size()).isEqualTo(1);

    HtmlPage creationPage = page.getElementByName("create_app").click();

    assertThat(creationPage.getUrl().getPath()).isEqualToIgnoringCase("/application/create");
  }

  @Test
  public void createApplication() throws Exception {

    final String appName = "appName";
    final String dns = "dns";
    final String healthEndpoint = "healthEndpoint";

    HtmlPage page = this.webClient.getPage("/application/create");

    HtmlForm createAppForm = page.getFormByName("create_app_form");

    createAppForm.getInputByName("name").setValueAttribute(appName);
    createAppForm.getInputByName("dns").setValueAttribute(dns);
    createAppForm.getInputByName("healthEndpoint").setValueAttribute(healthEndpoint);

    HtmlPage applicationsPage = createAppForm.getInputByName("submit").click();

    assertThat(applicationsPage.getUrl().getPath()).isEqualToIgnoringCase("/application");

    ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);

    Mockito.verify(applicationService, Mockito.times(1)).createApplication(applicationArgumentCaptor.capture());

    final Application applicationCreated = applicationArgumentCaptor.getValue();

    Assertions.assertEquals(appName, applicationCreated.getName());
    Assertions.assertEquals(dns, applicationCreated.getDns());
    Assertions.assertEquals(healthEndpoint, applicationCreated.getHealthEndpoint());
    Assertions.assertNull(applicationCreated.getId());
  }
}