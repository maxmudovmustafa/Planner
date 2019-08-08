package com.example.myapplicatio.aralash

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.myapplicatio.db.reminder.ReminderModelView
import javax.inject.Inject

class ReminderViewFactory @Inject constructor(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReminderModelView::class.java)) {
            ReminderModelView(this.app) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}