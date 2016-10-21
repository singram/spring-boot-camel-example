package com.srai.gateway;

import com.srai.model.Voter;
import org.springframework.stereotype.Component;

import java.util.Random;

//WIP
@Component
public class ExternalVoterRegistrationGatewayImpl implements ExternalVoterRegistrationGateway {

  private static final int REGISTRATION_PROBABILITY = 70;
  private final Random rand = new Random();

  @Override
  public Voter verifyRegistration(Voter voter) {
    boolean registered = isRegistered(voter);
    voter.setRegistered(registered);
    return voter;
  }

  @Override
  public boolean isRegistered(Voter voter) {
    return rand.nextInt(99) < REGISTRATION_PROBABILITY;
  }

}
