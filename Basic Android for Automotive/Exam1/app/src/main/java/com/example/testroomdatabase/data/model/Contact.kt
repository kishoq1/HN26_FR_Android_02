package com.example.testroomdatabase.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, // Đổi thành 0 để tự tăng

    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "phone") var phone: String = "",
    @ColumnInfo(name = "email") var email: String = "",

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var avatar: ByteArray? = null
) : Serializable