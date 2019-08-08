package com.example.myapplicatio.db.reminder

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.example.myapplicatio.db.Data

class ReminderRepositor(var application: Application) {
    private val db by lazy {
        Data.getDatabase(application)
    }

    private val dao by lazy { db.remindrDao() }
    private var mAllUsers: List<ReminderEntity>
    private var mLiveUser: LiveData<List<ReminderEntity>>

    init {
        mAllUsers = dao.getUserInfo()
        mLiveUser = dao.getLiveUser()
    }

    fun getAllPeople(): List<ReminderEntity> {
        return mAllUsers
    }

    fun getLiveUsers(): LiveData<List<ReminderEntity>> {
        return mLiveUser
    }

    fun insert(entity: ReminderEntity) {
        insertAsync(dao).execute(entity)
    }


    fun delete(task: ReminderEntity) {
        dao.deleteUser(task)
//        updateAsync(dao).execute(entity)
    }


    companion object {
        class insertAsync(private var dao: ReminderDao) : AsyncTask<ReminderEntity, Any, Any>() {
            override fun doInBackground(vararg value: ReminderEntity?) {
                if (value[0] == null) return
                return dao.insertUser(value[0]!!)
            }
        }


        class getPeopleAsync(private var dao: ReminderDao) : AsyncTask<Any, Any, List<ReminderEntity>>() {
            override fun doInBackground(vararg value: Any?): List<ReminderEntity>? {
                return dao.getUserInfo()
            }
        }

        class updateAsync(private var dao: ReminderDao) : AsyncTask<ReminderEntity, Any, Any>() {
            override fun doInBackground(vararg value: ReminderEntity?) {
                if (value[0] == null) return
                return dao.update(value[0]!!)
            }
        }

        class updatePeople(private var dao: ReminderDao, var id: Int) : AsyncTask<ReminderEntity, Any, Any>() {
            override fun doInBackground(vararg value: ReminderEntity?) {
                if (value[0] == null) return
            }
        }

        class updateBitmap(private var dao: ReminderDao) : AsyncTask<ReminderEntity, Any, Any>() {
            override fun doInBackground(vararg value: ReminderEntity?) {
                if (value[0] == null) return
                return dao.update(value[0]!!)
            }
        }
    }

}