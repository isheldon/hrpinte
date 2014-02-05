package com.eabax.hospital.integration.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class InTaskRepository {
  static final Logger LOG = LoggerFactory.getLogger(InTaskRepository.class);

  @Autowired
  JdbcTemplate eabaxJdbc;

  @Autowired
  JdbcTemplate inteJdbc;
  
  Long getPersonIdByNo(String no) {
    // TODO: coding
    return null;
  }
  
  Long getUnitIdByName(String name) {
    // TODO: coding
    return null;
  }
  
  Long getNextSeqValue(String seq) {
    return eabaxJdbc.queryForObject("select " + seq + ".nextVal from dual", Long.class);
  }
}
