package com.example.myapplicatio.task.schedule;

import android.content.Context;


import com.example.myapplicatio.common.base.task.BaseAsyncTask;
import com.example.myapplicatio.common.bean.Schedule;
import com.example.myapplicatio.common.data.ScheduleDao;
import com.example.myapplicatio.common.listener.OnTaskFinishedListener;

import java.util.List;


public class LoadScheduleTask extends BaseAsyncTask<List<Schedule>> {

    private int mYear;
    private int mMonth;
    private int mDay;

    public LoadScheduleTask(Context context, OnTaskFinishedListener<List<Schedule>> onTaskFinishedListener, int year, int month, int day) {
        super(context, onTaskFinishedListener);
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    @Override
    protected List<Schedule> doInBackground(Void... params) {
        ScheduleDao dao = ScheduleDao.getInstance(mContext);
        return dao.getScheduleByDate(mYear, mMonth,mDay);
    }
}
