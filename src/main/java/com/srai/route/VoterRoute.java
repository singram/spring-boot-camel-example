package com.srai.route;

import com.srai.model.ZipCodes;
import com.srai.predicate.ValidZipCodePredicate;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoterRoute extends RouteBuilder {

  @Autowired
  private ZipCodes zipCodes;

  @Override
  public void configure() throws Exception {
    from("seda:batchVoterChannel")
    .log(LoggingLevel.INFO, "Voter processing STARTED")
    .split(body())
    .to("seda:singleVoterChannel");

    from("seda:singleVoterChannel")
    .filter(new ValidZipCodePredicate(zipCodes))
    .to("seda:validVoterZipCodeChannel");

    from("seda:validVoterZipCodeChannel")
    .choice()
    .when().simple("${body.isRegistered}")
    .to("seda:registeredVoterChannel")
    .otherwise()
    .to("seda:unregisteredVoterChannel");

    from("seda:registeredVoterChannel")
    .to("stream:out");

    from("seda:unregisteredVoterChannel")
    .to("stream:out");

  }
}
