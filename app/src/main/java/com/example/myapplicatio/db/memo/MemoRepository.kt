package com.example.myapplicatio.db.memo

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.example.myapplicatio.db.Data

class MemoRepository(application: Application) {
    private val db by lazy {
        Data.getDatabase(application)
    }

    private val dao by lazy { db.memoDao() }
    private var mAllUsers: List<MemoEntity>? = null
    private var mLiveUser: LiveData<List<MemoEntity>>

    init {
        mLiveUser = dao.getLiveNote()
        mAllUsers = mLiveUser.value
    }

    fun getAllMemo(): List<MemoEntity> {
        return mAllUsers!!
    }

    fun getLiveMemo(): LiveData<List<MemoEntity>> {
        return mLiveUser
    }

    fun insert(entity: MemoEntity) {
        insertAsync(dao).execute(entity)
    }

    fun update(entity: MemoEntity) {
        updateAsync(dao).execute(entity)
    }

    fun updateMemo(entity: MemoEntity, id: Int) {
        updatePeople(dao, id).execute(entity)
    }


    fun updateBitmap(entity: MemoEntity) {
        updateBitmap(dao).execute(entity)
    }

    fun delete(task: MemoEntity) {
        dao.deleteNot(task)
//        updateAsync(dao).execute(entity)
    }

    fun getPeople(id: Int): MemoEntity? {
//        return dao.getTask(id)
        return getPeopleAsync(dao).execute(id).get()
    }

    companion object {
        class insertAsync(private var dao: MemoDao) : AsyncTask<MemoEntity, Any, Any>() {
            override fun doInBackground(vararg value: MemoEntity?) {
                if (value[0] == null) return
                return dao.insertNote(value[0]!!)
            }
        }


        class getPeopleAsync(private var dao: MemoDao) : AsyncTask<Int, Any, MemoEntity>() {
            override fun doInBackground(vararg value: Int?): MemoEntity? {
                if (value[0] == null) return null
                return dao.getNote(value[0]!!)
            }
        }

        class updateAsync(private var dao: MemoDao) : AsyncTask<MemoEntity, Any, Any>() {
            override fun doInBackground(vararg value: MemoEntity?) {
                if (value[0] == null) return
                return dao.update(value[0]!!)
            }
        }

        class updatePeople(private var dao: MemoDao, var id: Int) : AsyncTask<MemoEntity, Any, Any>() {
            override fun doInBackground(vararg value: MemoEntity?) {
                if (value[0] == null) return
//                return dao.updatePeople(value[0]!!.name,
//                        value[0]!!.password,
//                        value[0]!!.detail,
//                        value[0]!!.statePassword,
//                        value[0]!!.image!!,
//                        id)
            }
        }

        class updateBitmap(private var dao: MemoDao) : AsyncTask<MemoEntity, Any, Any>() {
            override fun doInBackground(vararg value: MemoEntity?) {
                if (value[0] == null) return
//                return dao.update(value[0]!!.image!!, value[0]!!.id)
            }
        }
    }
}
