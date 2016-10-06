package com.srai.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class VoterRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("seda:batchVoterChannel")
    .log(LoggingLevel.INFO, "Voter processing STARTED")
    .split(body())
    .to("stream:out");
  }
}
