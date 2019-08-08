package com.example.myapplicatio.db.user

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user_info")
    fun getUserInfo(): List<UserEntity>

    @Query("SELECT * FROM user_info")
    fun getLiveUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user_info where id = :userId")
    fun getUserInfo(userId: Int): UserEntity

    @Insert
    fun insertUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

    @Query("DELETE FROM user_info")
    fun deleteAll()

    @Query("UPDATE user_info SET image= :bitmap where id =:id")
    fun updateBitmap(bitmap: ByteArray, id: Int)

    @Update
    fun update(userEntity: UserEntity)

    @Query("update user_info set name = :name, password = :passwrod, detail = :detail, statePassword = :state, image = :bitmap where id = :id")
    fun updatePeople(name: String, passwrod: String, detail: String, state: Boolean, bitmap: ByteArray, id: Int)
}