package com.eabax.hospital.integration.task.model;

public class Department {
  public Long id;
  public String number;
  public String name;
  public String mmDeptNo;

  public Department(Long id, String number, String name) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.mmDeptNo = number;
  }

  @Override
  public String toString() {
    return "Department [id=" + id + ", number=" + number + ", mmDeptNo=" + mmDeptNo + ", name=" + name
        + "]";
  }
  
}
