package com.example.myapplicatio.task.schedule;

import android.content.Context;

import com.example.myapplicatio.common.base.task.BaseAsyncTask;
import com.example.myapplicatio.common.bean.Schedule;
import com.example.myapplicatio.common.data.ScheduleDao;
import com.example.myapplicatio.common.listener.OnTaskFinishedListener;


public class UpdateScheduleTask extends BaseAsyncTask<Boolean> {

    private Schedule mSchedule;

    public UpdateScheduleTask(Context context, OnTaskFinishedListener<Boolean> onTaskFinishedListener, Schedule schedule) {
        super(context, onTaskFinishedListener);
        mSchedule = schedule;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (mSchedule != null) {
            ScheduleDao dao = ScheduleDao.getInstance(mContext);
            return dao.updateSchedule(mSchedule);
        } else {
            return false;
        }
    }
}
