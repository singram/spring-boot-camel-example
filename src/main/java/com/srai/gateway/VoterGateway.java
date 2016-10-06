package com.srai.gateway;

import com.srai.model.Voter;

import java.util.Collection;

public interface VoterGateway {
  void collectVoters(Collection<Voter> voters);
}
