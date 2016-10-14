package com.srai.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.redis.RedisConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;


@Component
public class VoterOutputRoute extends RouteBuilder {

  @Bean
  public RedisSerializer stringSerializer() {
    return new StringRedisSerializer();
  }

  @Override
  public void configure() throws Exception {
    from("seda:processedVoterChannel")
    .multicast()
    .parallelProcessing()

    // Output registered voters to websocket
    .to("websocket://localhost:9292/echo?sendToAll=true")

    // Aggregate votes by candidate to REDIS
    .pipeline()
    .aggregate(simple("${body.getCandidate}"), new VoteCountAggregationStrategy()).completionTimeout(1000L)
    .setHeader(RedisConstants.COMMAND, simple("INCRBY"))
    .setHeader(RedisConstants.VALUE, simple("${body.count}"))
    .setHeader(RedisConstants.KEY, simple("${body.candidate}"))
    .to("spring-redis://{{redis.hostname}}:{{redis.port}}?serializer=#stringSerializer")
    .end()

    // Aggregate votes according to original batch.  Split for simple stream processing.
    .pipeline()
    .aggregate(header("batch_timestamp"), new ArrayListAggregationStrategy()).completionTimeout(500L)
    .split(body())
    .to("stream:out")
    .end();
  }

}