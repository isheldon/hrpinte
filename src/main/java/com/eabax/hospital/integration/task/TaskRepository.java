package com.eabax.hospital.integration.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class TaskRepository {

  @Autowired
  JdbcTemplate eabaxJdbc;

  @Autowired
  JdbcTemplate inteJdbc;
  
  public void foo() {
    try {
    eabaxJdbc.execute("select * from foo_eabax");
    } catch (Exception e) {
      System.out.println("EABAX DS worked.....................!");
    }
    try {
    inteJdbc.execute("select * from foo_inte");
    } catch (Exception e) {
      System.out.println("INTE DS worked.....................!");
    }
  }
}
