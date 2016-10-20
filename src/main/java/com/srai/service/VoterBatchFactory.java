package com.srai.service;

import com.srai.gateway.VoterGatewayImpl;
import com.srai.model.Voter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class VoterBatchFactory {

  @Setter
  @Value("${voter.batch.size:10}")
  private int batchSize;

  @Autowired
  VoterFactory voterFactory;

  @Autowired
  VoterGatewayImpl voterGateway;

  @Scheduled(fixedRate = 4000)
  public void scheduleVoterHandling() {
    Collection<Voter> voters = nextVoterBatch(batchSize);
    log.info("===========> Sending " + voters.size() + " voters to the system");
    voters.forEach(voter -> log.debug(voter.toString()));
    voterGateway.collectVoters(voters);
  }

  private Collection<Voter> nextVoterBatch(int limit) {
    List<Voter> voters = new ArrayList<>();
    for (int i = 0; i < limit; i++) {
      voters.add(voterFactory.nextVoter());
    }
    return voters;
  }

}
