package com.example.myapplicatio.calendar.month


interface OnMonthClickListener {
    fun onClickThisMonth(year: Int, month: Int, day: Int)
    fun onClickLastMonth(year: Int, month: Int, day: Int)
    fun onClickNextMonth(year: Int, month: Int, day: Int)
}
