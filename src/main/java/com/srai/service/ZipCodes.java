package com.srai.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.srai.model.ZipCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ZipCodes {

  private Map<Integer, ZipCode> zipCodes = Collections.synchronizedMap(new HashMap<Integer, ZipCode>());

  ZipCodes() {
    String csvFile = "us_postal_codes.csv";

    CSVReader reader = null;
    log.info("Establishing ZipCode dictionary");
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream is = classloader.getResourceAsStream(csvFile);
      reader = new CSVReader(new InputStreamReader(is), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
      String[] line;
      while ((line = reader.readNext()) != null) {
        if(StringUtils.isEmpty(line[0])) {
          continue;
        }
        zipCodes.put(new Integer(line[0]), new ZipCode(line));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    log.info("Establishing ZipCode dictionary complete");
  }

  public boolean isValid(int zipCode) {
    return zipCodes.containsKey(new Integer(zipCode));
  }

  public boolean isWithinState(int zipCode, String stateAbr) {
    ZipCode zip = zipCodes.get(zipCode);
    return (zip == null ? false : zip.isWithinState(stateAbr));
  }

  public ZipCode get(int zipCode) {
    return zipCodes.get(zipCode);
  }
}

