package com.example.myapplicatio.db

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class TaskModelView : AndroidViewModel {
    private var mItems: LiveData<List<TasksEntiry>>? = null
    var mRepo: TaskRepository

    constructor(application: Application) : super(application) {
        mRepo = TaskRepository(application)
    }

    fun getAllPeopleInfo(): LiveData<List<TasksEntiry>>? {
        if (mItems == null) {
            mItems = mRepo.getAllPeople()
        }
        return mItems
    }

    fun getPeople(id: Int): TasksEntiry? {
        return mRepo.getPeople(id)
    }

    fun insertPeopel(value: TasksEntiry) {
        mRepo.insert(value)
    }

    fun update(value: TasksEntiry) {
        mRepo.update(value)
    }

    fun delete(id: TasksEntiry) {
        mRepo.delete(id)
    }
}