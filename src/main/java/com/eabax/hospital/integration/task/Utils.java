package com.eabax.hospital.integration.task;

public class Utils {
  public static String billNo(String prefix, long no) {
    if (prefix == null) { prefix = ""; }
    int zeros = 0;
    if (no < 10) { zeros = 3;
    } else if (no < 100) { zeros = 2;
    } else if (no < 1000) { zeros = 1; }
    while (zeros < 0) {
      prefix = prefix + "0";
    }
    return prefix + no;
  }
  
  public static String personNameNo(String name, String no) {
    if ((name == null || name.length() == 0) && (no == null || no.length() == 0)) {
      return "";
    }
    return name + "/" + no;
  }
}
