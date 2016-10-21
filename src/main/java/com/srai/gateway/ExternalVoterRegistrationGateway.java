package com.srai.gateway;

import com.srai.model.Voter;

// WIP
public interface ExternalVoterRegistrationGateway {

  public Voter verifyRegistration(Voter voter);

  public boolean isRegistered(Voter voter);

}
