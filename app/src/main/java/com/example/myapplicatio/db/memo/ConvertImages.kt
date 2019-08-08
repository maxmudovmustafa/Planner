package com.example.myapplicatio.db.memo

import android.arch.persistence.room.TypeConverter

object ConvertImages {
    @TypeConverter
    @JvmStatic
    fun stringToList(data: String?): List<ByteArray> {
        if (data == null) {
            return emptyList()
        }

        return data.split(":") as List<ByteArray>
    }

    @TypeConverter
    @JvmStatic
    fun listToString(someObjects: List<ByteArray>): String {
        var json = StringBuilder()
        someObjects.forEach {
            json.append(it)
            json.append(":")
        }

        return json.toString()
    }
}
