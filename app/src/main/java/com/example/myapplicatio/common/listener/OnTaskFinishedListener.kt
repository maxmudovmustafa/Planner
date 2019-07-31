package com.example.myapplicatio.common.listener


interface OnTaskFinishedListener<T> {
    fun onTaskFinished(data: T)
}
