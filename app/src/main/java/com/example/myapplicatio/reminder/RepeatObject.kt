package com.example.myapplicatio.reminder

import uz.greenwhite.lib.uzum.UzumAdapter
import uz.greenwhite.lib.uzum.UzumReader
import uz.greenwhite.lib.uzum.UzumWriter

class RepeatObject {
    var value: String? = ""

    fun RepeatObject(detail: String) {
        this.value = detail
    }

    fun RepeatObject(read: UzumReader): RepeatObject {
        RepeatObject(read.readString())
        return this
    }

    fun write(w: UzumWriter) {
        w.write(this.value)
    }

    val UZUM_ADAPTER: UzumAdapter<RepeatObject> = object : UzumAdapter<RepeatObject>() {
        override fun write(out: UzumWriter, value: RepeatObject) {
            value.write(out)
        }

        override fun read(read: UzumReader): RepeatObject {
            return RepeatObject(read)
        }
    }
}
