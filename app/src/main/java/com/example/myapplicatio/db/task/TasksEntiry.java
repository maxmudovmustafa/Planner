package com.example.myapplicatio.db.task;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "task_do")
public class TasksEntiry {

    @PrimaryKey(autoGenerate = true)
    public int taskId;
    public String taskName;
    public boolean scheduleState;
    public String scheduleTime;
    public String scheduleTimeEnd;
    public String scheduleYear;
    public String scheduleMonth;
    public String scheduleDay;
    public String schedule_location;

    public TasksEntiry(String taskName,
                       boolean scheduleState,
                       String scheduleTime,
                       String scheduleTimeEnd,
                       String scheduleYear,
                       String scheduleMonth,
                       String scheduleDay,
                       String schedule_location) {
        this.taskName = taskName;
        this.scheduleState = scheduleState;
        this.scheduleTime = scheduleTime;
        this.scheduleTimeEnd = scheduleTimeEnd;
        this.scheduleYear = scheduleYear;
        this.scheduleMonth = scheduleMonth;
        this.scheduleDay = scheduleDay;
        this.schedule_location = schedule_location;
    }

    public void setScheduleState(boolean scheduleState) {
        this.scheduleState = scheduleState;
    }

    public void setSchedule_location(String schedule_location) {
        this.schedule_location = schedule_location;
    }

    public void setScheduleTimeEnd(String scheduleTimeEnd) {
        this.scheduleTimeEnd = scheduleTimeEnd;
    }
}
