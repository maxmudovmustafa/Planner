package com.example.myapplicatio.aralash

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        fun getApplicationContext(context: Context) = context.applicationContext as App
    }
}