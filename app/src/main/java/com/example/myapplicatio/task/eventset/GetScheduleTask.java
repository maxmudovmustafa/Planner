package com.example.myapplicatio.task.eventset;

import android.content.Context;

import com.example.myapplicatio.common.base.task.BaseAsyncTask;
import com.example.myapplicatio.common.bean.Schedule;
import com.example.myapplicatio.common.data.ScheduleDao;
import com.example.myapplicatio.common.listener.OnTaskFinishedListener;


import java.util.List;


public class GetScheduleTask extends BaseAsyncTask<List<Schedule>> {

    private int mId;

    public GetScheduleTask(Context context, OnTaskFinishedListener<List<Schedule>> onTaskFinishedListener, int id) {
        super(context, onTaskFinishedListener);
        mId = id;
    }

    @Override
    protected List<Schedule> doInBackground(Void... params) {
        ScheduleDao dao = ScheduleDao.getInstance(mContext);
        return dao.getScheduleByEventSetId(mId);
    }

}
