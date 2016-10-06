package com.srai.service;

import com.srai.model.Voter;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class VoterFactory {

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

  private int zipCode() {
    return rand.nextInt(99960);
  }

}
