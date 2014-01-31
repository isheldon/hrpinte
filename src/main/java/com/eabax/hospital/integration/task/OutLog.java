package com.eabax.hospital.integration.task;

import java.sql.Timestamp;

class OutLog {
  Timestamp processTime;
  Long departmentId;

  public OutLog(Timestamp processTime, Long departmentId) {
    this.processTime = processTime;
    this.departmentId = departmentId;
  }
  
  public OutLog() {
    this.processTime = new Timestamp(System.currentTimeMillis());
  }

  @Override
  public String toString() {
    return "OutLog [processTime=" + processTime + ", departmentId="
        + departmentId + "]";
  }
}
