package com.example.myapplicatio.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder_info")
    fun getUserInfo(): List<ReminderEntity>

    @Query("SELECT * FROM reminder_info")
    fun getLiveUser(): LiveData<List<ReminderEntity>>

    @Query("SELECT * FROM reminder_info where id = :taskId")
    fun getUserInfo(taskId: Int): ReminderEntity

    @Insert
    fun insertUser(task: ReminderEntity)

    @Delete
    fun deleteUser(task: ReminderEntity)

    @Query("DELETE FROM reminder_info")
    fun deleteAll()

    @Update
    fun update(task: ReminderEntity)

}