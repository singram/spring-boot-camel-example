package com.srai;

import com.srai.model.ZipCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootCamelExampleApplication {

  @Autowired
  private ZipCodes zipcode;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootCamelExampleApplication.class, args);
  }
}
