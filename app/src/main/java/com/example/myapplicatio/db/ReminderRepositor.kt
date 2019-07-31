package com.example.myapplicatio.db

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

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
//        dao.insertUser(entity)
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


        class getPeopleAsync(private var dao: ReminderDao) : AsyncTask<Int, Any, ReminderEntity>() {
            override fun doInBackground(vararg value: Int?): ReminderEntity? {
                if (value[0] == null) return null
                return dao.getUserInfo(value[0]!!)
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