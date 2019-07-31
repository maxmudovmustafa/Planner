package com.example.myapplicatio.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface TasksDao {

    @Delete
    fun deleteTask(task: TasksEntiry)

    @Insert
    fun insertNewTask(task: TasksEntiry)

    @Query("SELECT * FROM task_do where taskId = :id")
    fun getTask(id: Int) : TasksEntiry

    @Update
    fun updateTask(task: TasksEntiry)


    @Query("SELECT * FROM task_do ORDER BY taskName ASC")
    fun getAllTasks(): LiveData<List<TasksEntiry>>

    @Query("SELECT * FROM task_do WhERE scheduleState = :show")
    fun showDoneTasks(show: Boolean): List<TasksEntiry>

}