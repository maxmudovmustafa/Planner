package com.example.myapplicatio.db

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class ReminderModelView : AndroidViewModel {
    private var mItems: LiveData<List<ReminderEntity>>? = null
    var mRepo: ReminderRepositor

    constructor(application: Application) : super(application) {
        mRepo = ReminderRepositor(application)
    }

    fun getAllPeopleInfo(): LiveData<List<ReminderEntity>>? {
        if (mItems == null) {
            mItems = mRepo.getLiveUsers()
        }
        return mItems
    }

    fun insert(id: ReminderEntity) {
        mRepo.insert(id)
    }

    fun delete(id: ReminderEntity) {
        mRepo.delete(id)
    }
}