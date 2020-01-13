package com.example.myapplicatio.db.memo

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo_info")
    fun listNote(): List<MemoEntity>

    @Query("SELECT * FROM memo_info")
    fun getLiveNote(): LiveData<List<MemoEntity>>

    @Query("SELECT * FROM memo_info where id = :taskId")
    fun getNote(taskId: Int): MemoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(task: MemoEntity)

    @Delete
    fun deleteNot(task: MemoEntity)

    @Query("DELETE FROM memo_info")
    fun deleteAll()

    @Update
    fun update(task: MemoEntity)

}