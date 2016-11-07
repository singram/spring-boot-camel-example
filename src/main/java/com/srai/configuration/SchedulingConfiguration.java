package com.srai.configuration;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.srai.service.VoterBatchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {

  @Autowired
  VoterBatchFactory voterBatchFactory;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.addTriggerTask(
        () -> voterBatchFactory.scheduleVoterHandling(),
        triggerContext -> {
          Instant nextTriggerTime = Instant.now().plus(4, SECONDS);
          return Date.from(nextTriggerTime);
        });
  }
}