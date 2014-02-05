package com.eabax.hospital.integration.task;

import java.sql.Timestamp;

class OutLog {
  Timestamp processTime;
  Long departmentId;
  Long disposibleItemId;

  public OutLog(Timestamp processTime, Long departmentId, Long disposibleItemId) {
    this.processTime = processTime;
    this.departmentId = departmentId;
    this.disposibleItemId = disposibleItemId;
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
