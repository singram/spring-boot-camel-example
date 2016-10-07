package com.srai;

import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootCamelExampleApplication {

  @Bean
  CamelContextConfiguration contextConfiguration() {
    return new CamelContextConfiguration() {
      @Override
      public void beforeApplicationStart(CamelContext context) {
        MetricsRoutePolicyFactory pf = new MetricsRoutePolicyFactory();
        pf.setUseJmx(true);
        context.addRoutePolicyFactory(pf);
      }

      @Override
      public void afterApplicationStart(CamelContext camelContext) {
      }

    };
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBootCamelExampleApplication.class, args);
  }
}
