package com.eabax.hospital.integration.task.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.eabax.hospital.integration.task.Utils;

public class ApplyActivityRm implements RowMapper<ApplyActivity> {

  @Override
  public ApplyActivity mapRow(ResultSet rs, int rowNum) throws SQLException {
    ApplyActivity activity = new ApplyActivity();
    activity.id = rs.getLong("lngdrawapplydetailid");
    activity.applyNumber = Utils.billNo(rs.getString("strreceiptno"), rs.getLong("lngreceiptno"));
    activity.applyDate = Date.valueOf(rs.getString("strdate"));
    activity.applyDeptNo = rs.getString("strdepartmentcode");
    activity.applyPerson = Utils.personNameNo(
        rs.getString("stroperatorname"), rs.getString("stroperatorcode"));
    activity.setApproveDate(rs.getString("strapprovedate"));
    activity.approvePerson = Utils.personNameNo(
        rs.getString("approver_name"), rs.getString("approver_code"));
    activity.itemName = rs.getString("stritemname");
    activity.itemNo = rs.getString("stritemcode");
    activity.itemUnit = rs.getString("strunitname");
    activity.itemQty = rs.getInt("dblapplyquantity");
    activity.receiverPerson = Utils.personNameNo(
        rs.getString("receiver_name"), rs.getString("receiver_code"));
    int appType = rs.getInt("lngcustomtextid2");
    if (appType == 100) activity.applyType = 1; //借用
    if (appType == 101) activity.applyType = 2; //更换
    if (appType == 102) activity.applyType = 3; //领用
    if (appType == 103) activity.applyType = 4; //退库
    return activity;
  }

}
