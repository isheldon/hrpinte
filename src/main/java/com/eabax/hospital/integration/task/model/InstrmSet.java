package com.eabax.hospital.integration.task.model;

import java.math.BigDecimal;

public class InstrmSet {
  public String no;
  public String name;
  public String unit;
  public BigDecimal price;

  @Override
  public String toString() {
    return "InstrmSet [no=" + no + ", name=" + name + ", unit=" + unit
        + ", price=" + price + "]";
  }
}
