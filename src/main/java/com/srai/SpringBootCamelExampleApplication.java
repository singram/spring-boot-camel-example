package com.srai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootCamelExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootCamelExampleApplication.class, args);
  }
}
