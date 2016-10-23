package com.srai.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Voter {
  private final int age;
  @NonNull
  private boolean registered;
  private final String candidate;
  private final ZipCode zipCode;
  private int count=1;
}
