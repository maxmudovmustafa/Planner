package com.example.myapplicatio.db

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class UserRepository(application: Application) {
    private val db by lazy {
        Data.getDatabase(application)
    }

    private val dao by lazy { db.userDao() }
    private var mAllUsers: List<UserEntity>
    private var mLiveUser: LiveData<List<UserEntity>>

    init {
        mAllUsers = dao.getUserInfo()
        mLiveUser = dao.getLiveUser()
    }

    fun getAllPeople(): List<UserEntity> {
        return mAllUsers
    }

    fun getLiveUsers(): LiveData<List<UserEntity>> {
        return mLiveUser
    }

    fun insert(entity: UserEntity) {
//        dao.insertUser(entity)
        insertAsync(dao).execute(entity)
    }

    fun update(entity: UserEntity) {
        updateAsync(dao).execute(entity)
    }

    fun updatePeople(entity: UserEntity, id: Int) {
        updatePeople(dao, id).execute(entity)
    }


    fun updateBitmap(entity: UserEntity) {
        Companion.updateBitmap(dao).execute(entity)
    }

    fun delete(task: UserEntity) {
        dao.deleteUser(task)
//        updateAsync(dao).execute(entity)
    }

    fun getPeople(id: Int): UserEntity? {
//        return dao.getTask(id)
        return getPeopleAsync(dao).execute(id).get()
    }

    companion object {
        class insertAsync(private var dao: UserDao) : AsyncTask<UserEntity, Any, Any>() {
            override fun doInBackground(vararg value: UserEntity?) {
                if (value[0] == null) return
                return dao.insertUser(value[0]!!)
            }
        }


        class getPeopleAsync(private var dao: UserDao) : AsyncTask<Int, Any, UserEntity>() {
            override fun doInBackground(vararg value: Int?): UserEntity? {
                if (value[0] == null) return null
                return dao.getUserInfo(value[0]!!)
            }
        }

        class updateAsync(private var dao: UserDao) : AsyncTask<UserEntity, Any, Any>() {
            override fun doInBackground(vararg value: UserEntity?) {
                if (value[0] == null) return
                return dao.update(value[0]!!)
            }
        }

        class updatePeople(private var dao: UserDao, var id: Int) : AsyncTask<UserEntity, Any, Any>() {
            override fun doInBackground(vararg value: UserEntity?) {
                if (value[0] == null) return
                return dao.updatePeople(value[0]!!.name,
                        value[0]!!.password,
                        value[0]!!.detail,
                        value[0]!!.statePassword,
                        value[0]!!.image!!,
                        id)
            }
        }

        class updateBitmap(private var dao: UserDao) : AsyncTask<UserEntity, Any, Any>() {
            override fun doInBackground(vararg value: UserEntity?) {
                if (value[0] == null) return
                return dao.updateBitmap(value[0]!!.image!!, value[0]!!.id)
            }
        }
    }
}
