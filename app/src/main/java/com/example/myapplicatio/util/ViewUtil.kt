package com.example.myapplicatio.util

import android.view.View

object ViewUtil {
    fun visible(status: Boolean, view: View) {
        if (status)
            view.visibility = View.GONE
        else
            view.visibility = View.VISIBLE
    }

}