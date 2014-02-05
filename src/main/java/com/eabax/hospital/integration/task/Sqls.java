package com.eabax.hospital.integration.task;

class Sqls {

  static String selLastOutLog = "select top 1 * from EabaxOutLog order by id desc";

  static String insOutLog = "insert into EabaxOutLog (process_time, department_id) values (?, ?)";

  static String selDept = "select lngdepartmentid, strdepartmentcode, strfullname from department "
      + "where lngdepartmentid > ? order by lngdepartmentid";

  static String insDept = "insert into department (number, name) values (?, ?)";


  static String selDisposibleItems = 
      "select i.lngitemid, i.stritemcode, i.stritemname, i.strpackunit, i.stritemstyle, '' as itemmodel, "
      + "c.strcustomercode, c.strcustomername, i.strmadefactname, i.strregisterno "
      + "from item i, itemtype it, customer c "
      + "where i.lngitemtypeid = it.lngitemtypeid "
      + "and i.lngcustomerid = c.lngcustomerid "
      + "and (it.stritemtypecode like '1-1-05%' or it.stritemtypecode like '1-1-06%') "
      + "and i.lngitemid > ? "
      + "order by i.lngitemid";
  
  static String insDisposibleItem = "insert into DisposableItem "
      + "(number, name, unit, specification, model, supplier_name, supplier_no, "
      + "producer_name, registration_no, purchase_price, sales_price) "
      + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  static String selSuppliers = "select lngcustomerid, strcustomercode, strcustomername, strcontactname, "
        + "strofficephonenumber, strbilltoaddress, '' as nature, '' as legal_person "
        + "from customer where lngcustomerid > ? order by lngcustomerid";

  public static String insSupplier = "insert into Supplier "
      + "(number, name, contact, contact_phone, address, enterprise_nature, legal_person) "
      + "values (?, ?, ?, ?, ?, ?, ?)";
}
