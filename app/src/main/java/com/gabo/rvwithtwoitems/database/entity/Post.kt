package com.gabo.rvwithtwoitems.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Posts")
data class Post(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "fullName") var fullName: String = "",
    @ColumnInfo(name = "mainText") var mainText: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "hasImage") var hasImage: Boolean = false
) : Parcelable