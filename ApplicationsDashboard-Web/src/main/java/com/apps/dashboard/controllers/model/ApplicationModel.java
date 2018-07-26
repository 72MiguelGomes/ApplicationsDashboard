package com.apps.dashboard.controllers.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationModel {

  private Long id;

  private String name;

  private String dns;

  private boolean healthy;

  private String version;

  private String healthEndpoint;

}
