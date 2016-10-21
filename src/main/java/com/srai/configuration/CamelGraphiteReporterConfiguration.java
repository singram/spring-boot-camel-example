package com.srai.configuration;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class CamelGraphiteReporterConfiguration {

  @Autowired
  private MetricRegistry metricRegistry;

  @Value("${graphite.host}")
  private String graphiteHostname;

  @Value("${graphite.port}")
  private int graphitePort;

  @Bean(destroyMethod = "stop")
  @ConditionalOnExpression("${metrics.reporting.enabled:false}")
  public GraphiteReporter graphiteReporter() {
    final GraphiteSender graphite = new Graphite(new InetSocketAddress(graphiteHostname, graphitePort));
    final GraphiteReporter reporter = GraphiteReporter
        .forRegistry(metricRegistry)
        .prefixedWith("camel-spring-boot")
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(graphite);
    reporter.start(1, TimeUnit.SECONDS);
    return reporter;
  }

  @Bean
  @ConditionalOnExpression("${metrics.reporting.enabled:false}")
  CamelContextConfiguration contextConfiguration() {
    return new CamelContextConfiguration() {
      @Override
      public void beforeApplicationStart(CamelContext context) {
        MetricsRoutePolicyFactory pf = new MetricsRoutePolicyFactory();
        pf.setUseJmx(true);
        pf.setMetricsRegistry(metricRegistry);
        context.addRoutePolicyFactory(pf);
      }

      @Override
      public void afterApplicationStart(CamelContext camelContext) {
        log.info("Camel metrics with GraphiteReporter initiated");
      }

    };
  }

}
