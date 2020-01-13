package com.example.myapplicatio.db.user

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class UserViewFactory @Inject constructor(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserModelView::class.java)) {
            UserModelView(this.app) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}