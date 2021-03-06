package com.eabax.hospital.integration.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eabax.hospital.integration.task.model.InLog;
import com.eabax.hospital.integration.task.model.InstrmSet;
import com.eabax.hospital.integration.task.model.MmActivity;
import com.eabax.hospital.integration.task.model.OutLog;
import com.eabax.hospital.integration.task.model.RowMappers;

@Repository
@Transactional(readOnly = true)
public class InTaskRepository {
  static final Logger LOG = LoggerFactory.getLogger(InTaskRepository.class);

  @Autowired
  JdbcTemplate eabaxJdbc;

  @Autowired
  JdbcTemplate inteJdbc;
  
  /**
   * Retrieve data to be sync from Inte DB
   * @param data Container of Inte data
   */
  public void constructMmData(MmData data) {
    data.currentSyncTime = new Timestamp(System.currentTimeMillis());
    InLog log = getLastInLog();
    data.lastLog = log;
    LOG.debug("Last in log: " + log);
    
    List<InstrmSet> sets = this.getInteInstrmSets(log);
    this.refineInstrmSets(sets);
    data.instrmSets = sets;
    
    data.maxInReceiptNo = this.getMaxReceiptNo(11L, "GYS");
    data.maxOutReceiptNo = this.getMaxReceiptNo(21L, "GYS");
    
    List<MmActivity> activities = this.getInteMmActivities(log);
    this.refineMmActivities(activities);
    data.mmActivities = activities;
  }
  
  /**
   * Write MM data to EabaxDb
   * @param data MM data container
   * @param log Last inLog
   */
  @Transactional
  public void writeToEabaxDb(MmData data) {
    boolean hasNew = false;

    Long lastInstrmSetId = data.lastLog.instrmSetId;
    if (data.instrmSets.size() > 0) {
      lastInstrmSetId = this.writeEabaxInstrmSets(data.instrmSets);
      hasNew = true;
      LOG.debug("Instrument sets cynced.");
    }
    
    Long lastMmActivityId = data.lastLog.mmActivityId;
    if (data.mmActivities.size() > 0) {
      lastMmActivityId = this.writeEabaxActivities(
          data.mmActivities, data.maxInReceiptNo, data.maxOutReceiptNo);
      hasNew = true;
      LOG.debug("MmActivities cynced.");
    }

    if (hasNew) {
      InLog newLog = new InLog();
      newLog.processTime = data.currentSyncTime;
      newLog.instrmSetId = lastInstrmSetId;
      newLog.mmActivityId = lastMmActivityId;
      this.writeInLog(newLog);
      LOG.debug("New in log created: " + newLog);
    }
  }
  
  private InLog getLastInLog() {
    return inteJdbc.queryForObject(Sqls.selLastInLog, RowMappers.inLog);
  }
  
  private List<InstrmSet> getInteInstrmSets(InLog log) {
    return inteJdbc.query(Sqls.selInstrmSets, new Object[] { log.processTime }, RowMappers.instrmSet);
  }
  
  private Long writeEabaxInstrmSets(List<InstrmSet> sets) {
    for (InstrmSet set: sets) {
      eabaxJdbc.update(Sqls.insInstrmSet,
          new Object[] {set.no, set.name,
          set.unitId, set.unitId, set.unitId, set.unitId,
          set.price, set.price} );
    }
    return sets.get(sets.size() - 1).id;
  }

  private void refineInstrmSets(List<InstrmSet> sets) {
    for (InstrmSet set:sets) {
      set.unitId  = this.getUnitIdByName(set.unit);
    }
  }
  
  private List<MmActivity> getInteMmActivities(InLog log) {
    return inteJdbc.query(Sqls.selMmActivities, 
        new Object[] { log.processTime }, RowMappers.mmActivity);
  }
  
  private void refineMmActivities(List<MmActivity> activities) {
    for (MmActivity act: activities) {
      act.seqValue = this.getNextSeqValue("itemactivity_seq");
      Map<String, Long> info = this.getItemInfobyNo(act.itemNo);
      act.itemId = info.get("itemId");
      act.itemUnitId = info.get("unitId");
      act.itemPositionId = info.get("positionId");
      act.receiveDeptId = this.getDeptIdByNo(act.receiveDeptNo);
      act.approvePersonId = this.getPersonIdByNo(act.approvePersonNo);
      act.billmakerId = this.getPersonIdByNo(act.billmakerNo);
      act.billYear = Utils.getYear(act.billDate);
      act.billMonth = Utils.getMonth(act.billDate);
    }
  }
  
  private Long writeEabaxActivities(List<MmActivity> activities, long maxInReceiptNo, long maxOutReceiptNo) {
    for (MmActivity act: activities) {
      if (act.activityTypeId.intValue() == 1) { // InstrumentSet
        act.activityTypeId = 10L;
        act.receiptTypeId = 11L;
        act.templateId = 1059L;
        act.useTypeId = 120;
        act.hrpStatus = 0;
        this.writeEabaxActivity(act, maxInReceiptNo + 1);
        maxInReceiptNo++;
      }

      act.activityTypeId = 19L;
      act.receiptTypeId = 21L;
      act.templateId = 10381L;
      act.useTypeId = 100;
      act.hrpStatus = 1;
      this.writeEabaxActivity(act, maxOutReceiptNo);
      maxOutReceiptNo++;
    }
    return activities.get(activities.size() - 1).id;
  }
  
  private void writeEabaxActivity(MmActivity act, long receiptNo) {
    eabaxJdbc.update(Sqls.insInOutActivity, new Object[] {
        act.seqValue, act.activityTypeId, act.receiptTypeId, act.templateId,
        act.receiveDeptId, act.billYear, act.billMonth, "GYS", receiptNo,
        Utils.dateString(act.billDate), Utils.dateString(act.billDate), Utils.dateString(act.billDate), 1L, 1.0D,
        act.billmakerId, act.approvePersonId, act.useTypeId, 1L, 2L, act.hrpStatus 
    });

    if (act.activityTypeId.intValue() == 1) { // InstrumentSet
      eabaxJdbc.update(Sqls.insInActivityDetail, new Object[] {
          act.seqValue, 1, act.itemId,
          act.itemUnitId, act.itemPositionId, act.itemQty, 
          Utils.dateString(act.produceDate), Utils.dateString(act.dueDate),
          act.itemPrice, act.itemPrice, act.itemAmount, act.itemAmount, act.itemAmount, act.itemAmount
      });
    } else {
      eabaxJdbc.update(Sqls.insOutActivityDetail, new Object[] {
          act.seqValue, 1, act.itemId,
          act.itemUnitId, act.itemPositionId, act.itemQty, 302,
          Utils.dateString(act.produceDate), Utils.dateString(act.dueDate), act.itemPrice, act.itemAmount
      });
    }
  }
  
  private Long getPersonIdByNo(String no) {
    return eabaxJdbc.queryForObject(Sqls.selOperatorName, new Object[] { no }, Long.class);
  }
  
  private Map<String, Long> getItemInfobyNo(String no) {
    return eabaxJdbc.queryForObject( Sqls.selItem, new Object[] { no }, 

        new RowMapper<Map<String, Long>>() {
          @Override
          public Map<String, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Long> info = new HashMap<String, Long>();
            info.put("id", rs.getLong("lngitemid"));
            info.put("unitId", rs.getLong("lngstockunitid"));
            info.put("positionId", rs.getLong("lngpositionid"));
            return info;
          }
        } );
  }
  
  private Long getUnitIdByName(String name) {
    return eabaxJdbc.queryForObject(Sqls.selUnitName, new Object[] { name }, Long.class);
  }

  /* Maybe not need this
  private Long getUnitGroupIdByName(String unitName) {
    return null;
  }
  */
  
  private Long getDeptIdByNo(String no) {
    return eabaxJdbc.queryForObject(Sqls.selDepartmentId, new Object[] {no}, Long.class);
  }
  
  private Long getMaxReceiptNo(Long receiptTypeId, String prefix) {
    Long no = eabaxJdbc.queryForObject(Sqls.selMaxReceiptNo, new Object[] { receiptTypeId, prefix }, Long.class);
    if (no == null) { return 0L; }
    else { return no; }
  }
  
  private Long getNextSeqValue(String seq) {
    return eabaxJdbc.queryForObject("select " + seq + ".nextval from dual", Long.class);
  }
  
  private void writeInLog(InLog log) {
    inteJdbc.update(Sqls.insInLog,
        new Object[] { log.processTime, log.instrmSetId, log.mmActivityId } );
  }
}
