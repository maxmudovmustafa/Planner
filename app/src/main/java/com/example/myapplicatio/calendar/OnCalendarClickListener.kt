package com.example.myapplicatio.calendar


interface OnCalendarClickListener {
    fun onClickDate(year: Int, month: Int, day: Int)
    fun onPageChange(year: Int, month: Int, day: Int)
}
