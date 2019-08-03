package com.example.myapplicatio.util

import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object CalendarUtil {
    fun HOURS(): String {
        return "" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) +":" + Calendar.getInstance().get(Calendar.MINUTE)
    }

    fun TIME(): String{
       return ""+Calendar.getInstance().time.time

    }
    fun HOUR(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    fun MINUTE(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    fun DAY(): String {
        return "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    fun MONTH(): String {
        return monthName(Calendar.getInstance().get(Calendar.MONTH))
    }

    fun MONTH(month: Int): String {
        return monthName(month)
    }

    fun DATE(): String{
        return "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "." +Calendar.getInstance().get(Calendar.MONTH) +
                "."+ "" + Calendar.getInstance().get(Calendar.YEAR)
    }
    private fun monthName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "July"
            7 -> "June"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Not exists"
        }
    }
}