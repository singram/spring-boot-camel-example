package com.srai.model;

import lombok.Data;

@Data
public class Voter {
  private final int age;
  private final boolean registered;
  private final String candidate;
  private final int zipCode;
  private int count=1;
}
