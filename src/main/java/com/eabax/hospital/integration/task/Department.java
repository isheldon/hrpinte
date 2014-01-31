package com.eabax.hospital.integration.task;

public class Department {
  Long id;
  String number;
  String name;

  public Department(Long id, String number, String name) {
    this.id = id;
    this.number = number;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Department [id=" + id + ", number=" + number + ", name=" + name
        + "]";
  }
  
}
