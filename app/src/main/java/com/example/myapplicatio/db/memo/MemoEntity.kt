package com.example.myapplicatio.db.memo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "memo_info")
class MemoEntity(var title: String,
                 var date_start: String,
                 var date_end: String,
                 var time_start: String,
                 var time_end: String,
                 var privacy: Boolean,
                 var times: Int,
                 var loction: Int,
                 var people: String,
                 var note: String,
//                 var images: List<ByteArray>,
                 var image: ByteArray,
                 var color: Int) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}