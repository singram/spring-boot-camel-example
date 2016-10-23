package com.srai.strategy;

import com.srai.model.Voter;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class VoteCountAggregationStrategy implements AggregationStrategy {

  @Override
  public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
    Voter newBody = newExchange.getIn().getBody(Voter.class);
    if (oldExchange == null) {
      Voter v = new Voter(0, true, newBody.getCandidate(), null);
      newExchange.getIn().setBody(v);
      return newExchange;
    } else {
      Voter v = oldExchange.getIn().getBody(Voter.class);
      v.setCount(v.getCount()+1);
      return oldExchange;
    }
  }
}