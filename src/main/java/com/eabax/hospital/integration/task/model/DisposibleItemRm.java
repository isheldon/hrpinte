package com.eabax.hospital.integration.task.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DisposibleItemRm implements RowMapper<DisposibleItem> {
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
