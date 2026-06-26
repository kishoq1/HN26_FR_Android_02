package com.example.assignment4.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "roomphotos", foreignKeys = [
    ForeignKey(
        entity = RoomType::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("roomtype_id"),
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index(value = ["roomtype_id"])])
data class RoomPhoto(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "roomtype_id") val roomTypeId : Int,
    @ColumnInfo(name = "image_data", typeAffinity = ColumnInfo.BLOB) val imageData : ByteArray? = null,
    @ColumnInfo(name = "is_default") var isDefault : Boolean = false
) : Serializable