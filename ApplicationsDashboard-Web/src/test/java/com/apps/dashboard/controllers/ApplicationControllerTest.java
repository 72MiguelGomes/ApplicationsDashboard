package com.apps.dashboard.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.apps.dashboard.controllers.config.MockCoreConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
}