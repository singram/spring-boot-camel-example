package com.srai.route;

import com.srai.strategy.ArrayListAggregationStrategy;
import com.srai.strategy.VoteCountAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.redis.RedisConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
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
    from("seda:registeredVoterChannel")
    .routeId("VoteResultProcessor")
    .multicast()
    .parallelProcessing()

    // Output registered voters to websocket
    .pipeline()
    .marshal().json(JsonLibrary.Jackson)
    .convertBodyTo(String.class)  // Avoids string serialization issues in websocket component.
    .to("websocket://localhost:9292/votes?sendToAll=true")
    //.to("ahc-ws://localhost:8080/votes?sendToAll=true")
    .end()

    // Aggregate votes by candidate to REDIS
    .pipeline()
    .aggregate(simple("${body.getCandidate}"), new VoteCountAggregationStrategy()).completionTimeout(1000L)
    .setHeader(RedisConstants.COMMAND, simple("INCRBY"))
    .setHeader(RedisConstants.VALUE, simple("${body.count}"))
    .setHeader(RedisConstants.KEY, simple("${body.candidate}"))
    .to("spring-redis://{{spring.redis.host}}:{{spring.redis.port}}?serializer=#stringSerializer")
    .end()

    // Aggregate votes according to original batch.  Split for simple stream processing.
    .pipeline()
    .aggregate(header("batch_timestamp"), new ArrayListAggregationStrategy()).completionTimeout(500L)
    .split(body())
    .to("stream:out")
    .end();
  }

}