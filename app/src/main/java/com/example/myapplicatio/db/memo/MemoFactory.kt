package com.example.myapplicatio.db.memo

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class MemoFactory @Inject constructor(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MemoModelView::class.java)) {
            MemoModelView(this.app) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}