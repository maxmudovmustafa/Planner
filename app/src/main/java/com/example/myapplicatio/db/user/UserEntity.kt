package com.example.myapplicatio.db.user

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "user_info")
class UserEntity(var name: String,
                 var password: String,
                 var detail: String){

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public var image: ByteArray? = null

    @PrimaryKey(autoGenerate = true)
    public var id: Int = 0

    public var statePassword : Boolean = false

    fun setBitmap(value: ByteArray) {
        this.image = value
    }

    fun getBitmap(): ByteArray? {
        return image
    }
}