package com.srai.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class VoterOutputRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("seda:processedVoterChannel")
    .multicast()
    .parallelProcessing()

    // Output registered voters to websocket
    .to("websocket://localhost:9292/echo?sendToAll=true")

    // Aggregate votes by candidate
    .pipeline()
    .aggregate(simple("${body.getCandidate}"), new VoteCountAggregationStrategy()).completionTimeout(1000L)
    .to("stream:out")
    .end()

    // Aggregate votes according to original batch.  Split for simple stream processing.
    .pipeline()
    .aggregate(header("batch_timestamp"), new ArrayListAggregationStrategy()).completionTimeout(500L)
    .split(body())
    .to("stream:out")
    .end();
  }

}