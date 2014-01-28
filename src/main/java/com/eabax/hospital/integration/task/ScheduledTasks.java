package com.eabax.hospital.integration.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ScheduledTasks {

  @Autowired
  private TaskRepository taskRepository;

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
      "HH:mm:ss");

  @Scheduled(fixedRate = 10000)
  public void reportCurrentTime() {
    taskRepository.foo();
    System.out.println("The time is now " + dateFormat.format(new Date()));
  }
}
