package com.srai.gateway;

import com.srai.model.Voter;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class VoterGatewayImpl implements VoterGateway{
  @Produce(uri = "seda:batchVoterChannel")
  ProducerTemplate producerTemplate;

  @Override
  public void collectVoters(Collection<Voter> voters) {
    producerTemplate.sendBody(voters);
  }

}
