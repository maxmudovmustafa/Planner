package com.example.myapplicatio.db.task

import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class TaskRepositor(var dao: TasksDao) {

    fun getAllTasks(): LiveData<List<TasksEntiry>> {
        return dao.getAllTasks()
    }

    fun insert(word: TasksEntiry) {
        insertAsyncTask(dao).execute(word)
    }

    fun delete(word: TasksEntiry) {
        dao.deleteTask(word)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: TasksDao) : AsyncTask<TasksEntiry, Void, Void>() {

        override fun doInBackground(vararg params: TasksEntiry): Void? {
            mAsyncTaskDao.insertNewTask(params[0])
            return null
        }
    }

}