package com.example.myapplicatio.common.bean

import java.io.Serializable

class EventSet : Serializable {

    var id: Int = 0
    var name: String? = null
    var color: Int = 0
    var icon: Int = 0
    var isChecked: Boolean = false
}
