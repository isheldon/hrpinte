package com.eabax.hospital.integration.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ScheduledTasks {
  static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

  @Autowired
  private OutTaskRepository taskRepository;

  @Scheduled(fixedRate = 60000)
  public void reportCurrentTime() {
    LOG.debug("Start sync from Eabax to integration DB...");
    LOG.debug("..........................................");
    EabaxData data = new EabaxData();
    taskRepository.constructEabaxData(data);
    // TODO enable this taskRepository.writeToInteDb(data);
    LOG.debug("..........................................");
    LOG.debug("End sync from Eabax to integration DB...");
  }
}
