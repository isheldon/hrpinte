package com.eabax.hospital.integration.task;

import java.sql.Timestamp;
import java.util.List;

import com.eabax.hospital.integration.task.model.*;

public class MmData {
  Timestamp currentSyncTime;
  InLog lastLog;
  List<InstrmSet> instrmSets;
  List<MmActivity> mmActivities;
}
