package com.eabax.hospital.integration.task;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eabax.hospital.integration.task.model.InLog;
import com.eabax.hospital.integration.task.model.InstrmSet;
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
    
    // Instrument sets from InteDb to EabaxDB
    List<InstrmSet> sets = this.getInteInstrmSets(log);
    this.refineInstrmSets(sets);
    data.instrmSets = sets;
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

    if (hasNew) {
      InLog newLog = new InLog();
      newLog.processTime = data.currentSyncTime;
      newLog.instrmSetId = lastInstrmSetId;
      //TODO in out id
      this.writeInLog(newLog);
      LOG.debug("New in log created: " + newLog);
    }
  }
  
  private InLog getLastInLog() {
    return inteJdbc.queryForObject(Sqls.selLastInLog, RowMappers.inLog);
  }
  
  private List<InstrmSet> getInteInstrmSets(InLog log) {
    return inteJdbc.query(Sqls.selInstrmSets, RowMappers.instrmSet);
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
  
  private void refineMmActivities() {
    
  }

  private Long getPersonIdByNo(String no) {
    return eabaxJdbc.queryForObject(Sqls.selOperatorName, new Object[] { no }, Long.class);
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
    // TODO: coding
    return null;
  }
  
  private Long getMaxReceiptNo(Long receiptTypeId, String prefix) {
    // TODO: coding
    return null;
  }
  
  private Long getNextSeqValue(String seq) {
    return eabaxJdbc.queryForObject("select " + seq + ".nextVal from dual", Long.class);
  }
  
  private void writeInLog(InLog log) {
    inteJdbc.update(Sqls.insInLog,
        new Object[] { log.processTime, log.instrmSetId, log.outActivityId, log.inActivityId } );
  }
}
