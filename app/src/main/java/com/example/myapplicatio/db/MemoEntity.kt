package com.example.myapplicatio.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.ArrayList


@Entity(tableName = "reminder_info")
class MemoEntity(var title: String,
                 var date: String,
                 var time: String,
                 var state: Boolean,
                 var times:Int) {

    @PrimaryKey(autoGenerate = true)
    public var id: Int = 0

}