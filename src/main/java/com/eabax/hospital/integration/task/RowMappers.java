package com.eabax.hospital.integration.task;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.eabax.hospital.integration.task.model.*;

class RowMappers {
  static OutLogRm outLog = new OutLogRm();
  static DepartmentRm dept = new DepartmentRm();
  static DisposibleItemRm disposibleItem = new DisposibleItemRm(); 
}

class OutLogRm implements RowMapper<OutLog> {
  @Override
  public OutLog mapRow(ResultSet rs, int num) throws SQLException {
    // TODO add new column
    return new OutLog(
        rs.getTimestamp("process_time"), rs.getLong("department_id"), rs.getLong("disposible_item_id"));
  }
}

class DepartmentRm implements RowMapper<Department> {
  @Override
  public Department mapRow(ResultSet rs, int num) throws SQLException {
    return new Department(
        rs.getLong("lngdepartmentid"), rs.getString("strdepartmentcode"),
        rs.getString("strfullname"));
  }
}

class DisposibleItemRm implements RowMapper<DisposibleItem> {
  @Override
  public DisposibleItem mapRow(ResultSet rs, int rowNum) throws SQLException {
    DisposibleItem item = new DisposibleItem();
    item.id = rs.getLong("lngitemid");
    item.number = rs.getString("number");
    item.name = rs.getString("name");
    item.model = rs.getString("unit");
    item.setSpecification(rs.getString("specification"));
    item.setSupplierName(rs.getString("supplier_name"));
    item.setSupplierNo(rs.getString("supplier_no"));
    item.setProducerName(rs.getString("producer_name"));
    item.setRegistrationNo(rs.getString("registration_no"));
    item.purchasePrice = rs.getBigDecimal("purchase_price");
    item.salesPrice = rs.getBigDecimal("sales_price");
    return item;
  }
}
