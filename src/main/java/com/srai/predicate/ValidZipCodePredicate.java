package com.srai.predicate;

import com.srai.model.Voter;
import com.srai.service.ZipCodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

@Slf4j
public class ValidZipCodePredicate implements Predicate {

  private ZipCodes zipCodes;

  public ValidZipCodePredicate(ZipCodes zipcodes) {
    this.zipCodes = zipcodes;
  }

  @Override
  public boolean matches(Exchange exchange) {
    Voter voter = exchange.getIn().getBody(Voter.class);
    boolean validZipcode = zipCodes.isValid(voter.getZipCode());
    if (validZipcode) {
      log.info(voter.getZipCode()+" is valid");
    }
    return validZipcode;
  }

}