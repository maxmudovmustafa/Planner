package com.example.myapplicatio.db.user

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class UserModelView : AndroidViewModel {
    private var mItems: List<UserEntity> ? =null
    private var mLiveUsers: LiveData<List<UserEntity>> ? =null
    var mRepo: UserRepository

    constructor(application: Application) : super(application) {
        mRepo = UserRepository(application)
    }

    fun getAllPeopleInfo(): List<UserEntity> ?{
        if (mItems == null) {
            mItems = mRepo.getAllPeople()
        }
        return mItems
    }

    fun getLiveUsers(): LiveData<List<UserEntity>>? {
        if (mLiveUsers == null ) {
            mLiveUsers = mRepo.getLiveUsers()
        }
        return mLiveUsers
    }

    fun getPeople(id: Int): UserEntity? {
        return mRepo.getPeople(id)
    }

    fun insertPeopel(value: UserEntity) {
        mRepo.insert(value)
    }

    fun update(value: UserEntity) {
        mRepo.update(value)
    }

    fun updatePeople(value: UserEntity, id: Int){
        mRepo.updatePeople(value, id)
    }

    fun updateBitmap(value: UserEntity) {
        mRepo.updateBitmap(value)
    }

    fun delete(id: UserEntity) {
        mRepo.delete(id)
    }
}