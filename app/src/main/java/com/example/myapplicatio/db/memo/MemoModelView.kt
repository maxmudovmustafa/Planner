package com.example.myapplicatio.db.memo

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class MemoModelView : AndroidViewModel {
    private var mItems: List<MemoEntity> ? =null
    private var mLiveUsers: LiveData<List<MemoEntity>> ? =null
    var mRepo: MemoRepository

    constructor(application: Application) : super(application) {
        mRepo = MemoRepository(application)
    }

    fun getAllPeopleInfo(): List<MemoEntity> ?{
        if (mItems == null) {
            mItems = mRepo.getAllMemo()
        }
        return mItems
    }

    fun getLiveUsers(): LiveData<List<MemoEntity>>? {
        if (mLiveUsers == null ) {
            mLiveUsers = mRepo.getLiveMemo()
        }
        return mLiveUsers
    }

    fun getPeople(id: Int): MemoEntity? {
        return mRepo.getPeople(id)
    }

    fun insertPeopel(value: MemoEntity) {
        mRepo.insert(value)
    }

    fun update(value: MemoEntity) {
        mRepo.update(value)
    }

    fun updatePeople(value: MemoEntity, id: Int){
        mRepo.updateMemo(value, id)
    }

    fun updateBitmap(value: MemoEntity) {
        mRepo.updateBitmap(value)
    }

    fun delete(id: MemoEntity) {
        mRepo.delete(id)
    }
}