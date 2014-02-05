package com.eabax.hospital.integration.task;

import java.sql.Timestamp;

class OutLog {
  Timestamp processTime;
  Long departmentId;
  Long disposibleItemId;
  Long supplierId;
  Long activityId;

  public OutLog(Timestamp processTime, Long departmentId,
      Long disposibleItemId, Long supplierId, Long activityId) {
    this.processTime = processTime;
    this.departmentId = departmentId;
    this.disposibleItemId = disposibleItemId;
    this.supplierId = supplierId;
    this.activityId = activityId;
  }
  
  public OutLog() {
    this.processTime = new Timestamp(System.currentTimeMillis());
  }

  @Override
  public String toString() {
    return "OutLog [processTime=" + processTime + ", departmentId="
        + departmentId + ", disposibleItemId=" + disposibleItemId
        + ", supplierId=" + supplierId + ", activityId=" + activityId + "]";
  }

}
