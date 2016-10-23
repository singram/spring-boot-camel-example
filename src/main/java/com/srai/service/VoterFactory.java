package com.srai.service;

import com.srai.model.Voter;
import com.srai.model.ZipCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VoterFactory {

  @Autowired
  private ZipCodes zipCodes;

  private Random rand = new Random();

  public Voter nextVoter() {
    return new Voter(age(), registered(), candidate(), zipCode());
  }

  private int age(){
    return rand.nextInt(70)+18;
  }

  private boolean registered() {
    return rand.nextBoolean();
  }

  private String candidate() {
    return rand.nextBoolean() ? "Trump" : "Clinton";
  }

  private ZipCode zipCode() {
    return zipCodes.get(rand.nextInt(99960));
  }

}
