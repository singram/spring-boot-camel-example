package com.srai.gateway;

import com.srai.model.Voter;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ExternalVoterRegistrationGatewayImpl implements ExternalVoterRegistrationGateway {

  private final Random rand = new Random();

  @Override
  public Voter verifyRegistration(Voter voter) {
    boolean registered = isRegistered(voter);
    return new Voter(voter.getAge(), registered, voter.getCandidate(), voter.getZipCode());
  }

  @Override
  public boolean isRegistered(Voter voter) {
    return rand.nextBoolean();
  }

}
