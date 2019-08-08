package com.example.myapplicatio.db.task

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.example.myapplicatio.db.Data

class TaskRepository(application: Application) {
    private val db by lazy{
        Data.getDatabase(application)
    }

    private val dao by lazy { db.wordDao() }
    private var mAlltasks :LiveData<List<TasksEntiry>>

    init {
        mAlltasks = dao.getAllTasks()
    }

    fun getAllPeople(): LiveData<List<TasksEntiry>> {
        return mAlltasks
    }

    fun insert(entity: TasksEntiry) {
        dao.insertNewTask(entity)
        insertAsync(dao).execute(entity)
    }

    fun update(entity: TasksEntiry) {
        dao.updateTask(entity)
        updateAsync(dao).execute(entity)
    }

    fun delete(task: TasksEntiry) {
        dao.deleteTask(task)
//        updateAsync(dao).execute(entity)
    }

    fun getPeople(id: Int): TasksEntiry? {
//        return dao.getTask(id)
        return getPeopleAsync(dao).execute(id).get()
    }

    companion object {
         class insertAsync(private var dao: TasksDao) : AsyncTask<TasksEntiry, Any, Any>() {
             override fun doInBackground(vararg value: TasksEntiry?) {
                 if (value[0] == null) return
                 return dao.insertNewTask(value[0]!!)
             }
         }


         class getPeopleAsync(private var dao: TasksDao) : AsyncTask<Int, Any, TasksEntiry>() {
             override fun doInBackground(vararg value: Int?): TasksEntiry?{
                 if (value[0] == null) return null
                 return dao.getTask(value[0]!!)}
         }

         class updateAsync(private var dao: TasksDao) : AsyncTask<TasksEntiry, Any, Any>() {
             override fun doInBackground(vararg value: TasksEntiry?) {
                 if (value[0] == null) return
                 return dao.updateTask(value[0]!!)
             }
         }
     }
}
