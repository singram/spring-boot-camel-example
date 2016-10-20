package com.srai.route;

import com.srai.predicate.ValidZipCodePredicate;
import com.srai.service.ZipCodes;
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
    .routeId("VoterBatchSpliter")
    .setHeader("batch_timestamp", constant(Calendar.getInstance().getTimeInMillis()))
    .log(LoggingLevel.INFO, "Voter processing STARTED")
    .split(body())
    .to("seda:singleVoterChannel");

    from("seda:singleVoterChannel")
    .routeId("VoterZipcodeValidator")
    .filter(new ValidZipCodePredicate(zipCodes))
    .to("seda:votersWithValidZipCodeChannel");

    from("seda:votersWithValidZipCodeChannel")
    .routeId("VoterRegistrationSpliter")
    .choice()
    .when().simple("${body.isRegistered}")
    .to("seda:registrationValidationChannel")
    .otherwise()
    .to("seda:unregisteredVoterChannel");

    from("seda:registrationValidationChannel")
    .routeId("VoterRegistrationValidation")
    .transform().method("externalVoterRegistrationGatewayImpl", "verifyRegistration")
    .choice()
    .when().simple("${body.isRegistered}")
    .to("seda:registeredVoterChannel")
    .otherwise()
    .to("seda:unregisteredVoterChannel");

  }
}
