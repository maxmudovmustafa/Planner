//package com.example.myapplicatio.db
//
//import android.arch.lifecycle.LiveData
//import android.arch.lifecycle.ViewModel
//
//class ListCollectionItem(taskRepositor: TaskRepositor): ViewModel(){
//    var repositor :TaskRepositor ? =null
//    init {
//        this.repositor = taskRepositor
//    }
//
//    fun getAllRepos(): LiveData<List<TasksEntiry>>? {
//        return repositor?.getAllTasks()
//    }
//}