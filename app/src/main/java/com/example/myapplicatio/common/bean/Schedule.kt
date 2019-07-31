package com.example.myapplicatio.common.bean

import java.io.Serializable


class Schedule : Serializable {

    var id: Int = 0
    var color: Int = 0
    var title: String? = null
    var desc: String? = null
        get() = if (field == null) "" else field
    var location: String? = null
        get() = if (field == null) "" else field
    var state: Int = 0
    var time: Long = 0
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var eventSetId: Int = 0
}
