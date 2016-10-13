package com.srai.route;

import com.srai.model.ZipCodes;
import com.srai.predicate.ValidZipCodePredicate;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class VoterProcessingRoute extends RouteBuilder {

  @Autowired
  private ZipCodes zipCodes;

  @Override
  public void configure() throws Exception {
    from("seda:batchVoterChannel")
    .setHeader("batch_timestamp", constant(Calendar.getInstance().getTimeInMillis()))
    .log(LoggingLevel.INFO, "Voter processing STARTED")
    .split(body())
    .to("seda:singleVoterChannel");

    from("seda:singleVoterChannel")
    .filter(new ValidZipCodePredicate(zipCodes))
    .to("seda:validVoterZipCodeChannel");

    from("seda:validVoterZipCodeChannel")
    .choice()
    .when().simple("${body.isRegistered}")
    .to("seda:processedVoterChannel")
    .otherwise()
    .to("seda:unregisteredLocalVoterChannel");

    from("seda:unregisteredLocalVoterChannel")
    .transform().method("externalVoterRegistrationGatewayImpl", "verifyRegistration")
    .choice()
    .when().simple("${body.isRegistered}")
    .to("seda:processedVoterChannel");

  }
}
