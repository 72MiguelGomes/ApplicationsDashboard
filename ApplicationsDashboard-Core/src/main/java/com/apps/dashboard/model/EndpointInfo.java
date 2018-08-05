package com.apps.dashboard.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EndpointInfo {

  private String endpoint;

  private String result;

}
