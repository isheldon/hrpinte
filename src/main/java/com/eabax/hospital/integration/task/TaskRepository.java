package com.eabax.hospital.integration.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class TaskRepository {
  
  static final Logger LOG = LoggerFactory.getLogger(TaskRepository.class);

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
    data.departments = getEabaxDepartments(log);
    LOG.debug("Departments to be sync: " + data.departments);
  }
  
  /**
   * Write Eabax data to inteDb
   * @param data Eabax data container
   * @param log Last outLog
   */
  @Transactional
  public void writeToInteDb(EabaxData data) {
    boolean hasNew = false;
    Long lastDeptId = data.lastLog.departmentId;
    if (data.departments.size() > 0) {
      lastDeptId = this.writeInteDepartments(data.departments);
      hasNew = true;
      LOG.debug("Department cynced");
    }
    
    if (hasNew) {
      OutLog newLog = new OutLog();
      newLog.departmentId = lastDeptId;
      this.writeOutLog(newLog);
      LOG.debug("New log: " + newLog);
    }
  }
  
  private OutLog getLastOutLog() {
    String sql = "select top 1 * from EabaxOutLog order by id desc";
    return inteJdbc.queryForObject(sql, new OutLogRm());
  }
  
  private void writeOutLog(OutLog log) {
    String sql = "insert into EabaxOutLog (process_time, department_id) values (?, ?)";
    inteJdbc.update(sql, new Object[] { log.processTime, log.departmentId });
  }

  private List<Department> getEabaxDepartments(OutLog log) {
    String sql = "select lngdepartmentid, strdepartmentcode, strfullname from department "
        + "where lngdepartmentid > ? order by lngdepartmentid";
    return eabaxJdbc.query(sql, new Object[] {log.departmentId}, new DepartmentRm());
  }
  
  /**
   * Insert departments into inteDb
   * @param depts
   * @return last department id
   */
  private Long writeInteDepartments(List<Department> depts) {
    String sql = "insert into department (number, name) values (?, ?)";
    for (Department dept: depts) {
      inteJdbc.update(sql, new Object[] { dept.number, dept.name });
    }
    return depts.get(depts.size() - 1).id;
  }
  
}
  
class OutLogRm implements RowMapper<OutLog> {
  @Override
  public OutLog mapRow(ResultSet rs, int num) throws SQLException {
    return new OutLog(
        rs.getTimestamp("process_time"), rs.getLong("department_id"));
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
