package com.example.myapplicatio.task.schedule;

import android.content.Context;

import com.example.myapplicatio.common.base.task.BaseAsyncTask;
import com.example.myapplicatio.common.bean.Schedule;
import com.example.myapplicatio.common.data.ScheduleDao;
import com.example.myapplicatio.common.listener.OnTaskFinishedListener;


public class AddScheduleTask extends BaseAsyncTask<Schedule> {

    private Schedule mSchedule;

    public AddScheduleTask(Context context, OnTaskFinishedListener<Schedule> onTaskFinishedListener, Schedule schedule) {
        super(context, onTaskFinishedListener);
        mSchedule = schedule;
    }

    @Override
    protected Schedule doInBackground(Void... params) {
        if (mSchedule != null) {
            ScheduleDao dao = ScheduleDao.getInstance(mContext);
            int id = dao.addSchedule(mSchedule);
            if (id != 0) {
                mSchedule.setId(id);
                return mSchedule;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
