package com.example.myapplicatio.aralash

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.myapplicatio.db.user.UserModelView
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