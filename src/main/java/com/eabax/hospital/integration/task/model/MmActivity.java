package com.eabax.hospital.integration.task.model;

import java.math.BigDecimal;
import java.sql.Date;

public class MmActivity {
  public int dataType;
  public String itemNo;
  public String itemUnit;
  public String itemPrice;
  public String itemQty;
  public String itemAmount;
  public Date produceDate;
  public Date dueDate;
  public String receiveDeptNo;
  public String approvePersonNo;
  public BigDecimal feeAmount; // TODO: what does it mean
  public String billmakerNo;
  public Date billDate;

  @Override
  public String toString() {
    return "MmActivity [dataType=" + dataType + ", itemNo=" + itemNo
        + ", itemUnit=" + itemUnit + ", itemPrice=" + itemPrice + ", itemQty="
        + itemQty + ", itemAmount=" + itemAmount + ", produceDate="
        + produceDate + ", dueDate=" + dueDate + ", receiveDeptNo="
        + receiveDeptNo + ", approvePersonNo=" + approvePersonNo
        + ", feeAmount=" + feeAmount + ", billmakerNo=" + billmakerNo
        + ", billDate=" + billDate + "]";
  }
}
