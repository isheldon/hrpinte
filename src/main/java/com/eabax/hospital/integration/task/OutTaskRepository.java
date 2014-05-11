package com.eabax.hospital.integration.task;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eabax.hospital.integration.task.model.*;

@Repository
@Transactional(readOnly = true)
public class OutTaskRepository {
  
  static final Logger LOG = LoggerFactory.getLogger(OutTaskRepository.class);

  @Autowired
  JdbcTemplate eabaxJdbc;

  @Autowired
  JdbcTemplate inteJdbc;
  
  /**
   * Retrieve data to be sync from Eabax DB
   * @param data Container of Eabax data
   */
  public void constructEabaxData(EabaxData data) {
    OutLog log = getLastOutLog();
    data.lastLog = log;
    LOG.debug("Last log: " + log);
    data.departments = this.getEabaxDepartments(log);
    LOG.debug("Has " + data.departments.size() + " department(s) to be sync.");
    // LOG.debug("-----departments-----\n" + data.departments);
    data.disposibleItems = this.getEabaxDisposibleItems(log);
    LOG.debug("Has " + data.disposibleItems.size() + " disposibleItem(s) to be sync.");
    // LOG.debug("-----disposible items-----\n" + data.disposibleItems);
    data.suppliers = this.getEabaxSuppliers(log);
    LOG.debug("Has " + data.suppliers.size() + " supplier(s) to be sync.");
    // LOG.debug("-----suppliers-----\n" + data.suppliers);
    data.applyActivities = this.getEabaxApplyActivities(log);
    LOG.debug("Has " + data.applyActivities.size() + " applyActivities to be sync.");
    // LOG.debug("-----applyActivities-----\n" + data.applyActivities);
  }
  
  /**
   * Write Eabax data to inteDb
   * @param data Eabax data container
   * @param log Last outLog
   */
  @Transactional(value="inteTxManager",
      rollbackFor=RuntimeException.class, propagation=Propagation.REQUIRED)
  public void writeToInteDb(EabaxData data) {
    boolean hasNew = false;

    // sync departments
    Long lastDeptId = data.lastLog.departmentId;
    if (data.departments.size() > 0) {
      lastDeptId = this.writeInteDepartments(data.departments);
      hasNew = true;
      LOG.debug("Department cynced");
    }
    
    // sync disposibleItems
    Long lastItemId = data.lastLog.disposibleItemId;
    if (data.disposibleItems.size() > 0) {
      lastItemId = this.writeInteDisposibleItems(data.disposibleItems);
      hasNew = true;
      LOG.debug("Disposible items cynced");
    }
    
    // sync suppliers
    Long lastSupplierId = data.lastLog.supplierId;
    if (data.suppliers.size() > 0) {
      lastSupplierId = this.writeInteSuppliers(data.suppliers);
      hasNew = true;
      LOG.debug("Suppliers cynced");
    }
    
    //sync ApplyActivities
    Long lastActivityId = data.lastLog.applyActivityId;
    if (data.applyActivities.size() > 0) {
      lastActivityId = this.writeInteApplyActivities(data.applyActivities);
      hasNew = true;
      LOG.debug("Apply activities cynced");
    }
    
    if (hasNew) {
      OutLog newLog = new OutLog();
      newLog.departmentId = lastDeptId;
      newLog.disposibleItemId = lastItemId;
      newLog.supplierId = lastSupplierId;
      newLog.applyActivityId = lastActivityId;
      this.writeOutLog(newLog);
      LOG.debug("New log created: " + newLog);
    }
  }
  
  private OutLog getLastOutLog() {
    return inteJdbc.queryForObject(Sqls.selLastOutLog, RowMappers.outLog);
  }
  
  private void writeOutLog(OutLog log) {
    inteJdbc.update(Sqls.insOutLog, 
        new Object[] { 
        log.processTime, log.departmentId, log.disposibleItemId, log.supplierId, log.applyActivityId });
  }

  private List<Department> getEabaxDepartments(OutLog log) {
    List<Department> depts = eabaxJdbc.query(Sqls.selDept, new Object[] {log.departmentId}, RowMappers.dept);
    if (depts == null || depts.size() == 0) { return depts; }
    
    List<String> mmDetpNos = inteJdbc.queryForList(Sqls.selMmDeptNos, String.class);
    if (mmDetpNos == null || mmDetpNos.size() == 0) { return depts; }
    for (Department dept: depts) {
      if (mmDetpNos.contains(dept.number)) {
        dept.mmDeptNo = UUID.randomUUID().toString();
      }
    }
    return depts;
  }
  
  /**
   * Insert departments into inteDb
   * @param depts
   * @return last department id
   */
  private Long writeInteDepartments(List<Department> depts) {
    for (Department dept: depts) {
      inteJdbc.update(Sqls.insDept, new Object[] { dept.number, dept.name, dept.mmDeptNo });
    }
    return depts.get(depts.size() - 1).id;
  }
  
  private List<DisposibleItem> getEabaxDisposibleItems(OutLog log) {
    return eabaxJdbc.query(Sqls.selDisposibleItems,
        new Object[] { log.disposibleItemId }, RowMappers.disposibleItem);
  }
  
  private Long writeInteDisposibleItems(List<DisposibleItem> items) {
    for (DisposibleItem item:items) {
      inteJdbc.update(Sqls.insDisposibleItem, 
          new Object[] {item.number, item.name, item.unit, item.specification, item.model,
          item.supplierName, item.supplierNo, item.producerName, item.registrationNo,
          item.purchasePrice, item.salesPrice});
    }
    return items.get(items.size()-1).id;
  }
  
  private List<Supplier> getEabaxSuppliers(OutLog log) {
    return eabaxJdbc.query(Sqls.selSuppliers,
        new Object[] { log.supplierId }, RowMappers.supplier);
  }
  
  private Long writeInteSuppliers(List<Supplier> suppliers) {
    for (Supplier supplier: suppliers) {
      inteJdbc.update(Sqls.insSupplier,
          new Object[] {supplier.number, supplier.name, supplier.contact,
          supplier.contactPhone, supplier.address, supplier.nature, supplier.legalPerson});
    }
    return suppliers.get(suppliers.size() - 1).id;
  }
  
  private List<ApplyActivity> getEabaxApplyActivities(OutLog log) {
    List<ApplyActivity> acts = eabaxJdbc.query(Sqls.selApplyActivities,
        new Object[] { log.applyActivityId }, RowMappers.applyActivity);
    for (ApplyActivity act: acts) {
      String typeCode = this.getItemTypeCodeByItemNo(act.itemNo);
      if (typeCode.startsWith("1-1-05") || typeCode.startsWith("1-1-06")) {
        //一次性物品
        act.itemType = 2;
      } else {
        //器械包
        act.itemType = 1;
      }
    }
    return acts;
  }
  
  private Long writeInteApplyActivities(List<ApplyActivity> applyActivities) {
    for (ApplyActivity act: applyActivities) {
      inteJdbc.update(Sqls.insApplyActivity,
          new Object[] {act.detailId, act.applyNumber, act.applyDate, act.applyDeptNo, act.applyPerson,
          act.approveDate, act.approvePerson, act.itemName, act.itemType, act.itemNo, 
          act.itemUnit, act.itemQty, act.receiverPerson, act.applyType, 0});
    }
    return applyActivities.get(applyActivities.size() - 1).id;
  }

  private String getItemTypeCodeByItemNo(String itemNo) {
    return eabaxJdbc.queryForObject(Sqls.selItemTypeCode, new Object[] {itemNo}, String.class);
  }
}
