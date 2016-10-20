package com.srai.model;

import lombok.Data;

@Data
public class ZipCode {
  private int code;
  private String name;
  private String state;
  private String stateAbr;
  private String county;
  private float latitude;
  private float longitude;

  public ZipCode(String[] line) {
    code = new Integer(line[0]);
    name = line[1];
    state = line[2];
    stateAbr = line[3];
    county = line[4];
    latitude = new Float(line[5]);
    longitude = new Float(line[6]);
  }

  public boolean isWithinState(String stateAbr) {
    return this.getStateAbr().equals(stateAbr);
  }

}
