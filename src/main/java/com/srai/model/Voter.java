package com.srai.model;

import lombok.Data;

@Data
public class Voter {
  private final int age;
  private final boolean regstered;
  private final String vote;
  private final int zipCode;
}
