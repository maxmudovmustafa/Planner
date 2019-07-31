package com.example.myapplicatio.task.eventset;

import android.content.Context;

import com.example.myapplicatio.common.base.task.BaseAsyncTask;
import com.example.myapplicatio.common.data.EventSetDao;
import com.example.myapplicatio.common.data.ScheduleDao;
import com.example.myapplicatio.common.listener.OnTaskFinishedListener;


public class RemoveEventSetTask extends BaseAsyncTask<Boolean> {

    private int mId;

    public RemoveEventSetTask(Context context, OnTaskFinishedListener<Boolean> onTaskFinishedListener, int id) {
        super(context, onTaskFinishedListener);
        mId = id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ScheduleDao scheduleDao = ScheduleDao.getInstance(mContext);
        scheduleDao.removeScheduleByEventSetId(mId);
        EventSetDao dao = EventSetDao.getInstance(mContext);
        return dao.removeEventSet(mId);
    }
}
