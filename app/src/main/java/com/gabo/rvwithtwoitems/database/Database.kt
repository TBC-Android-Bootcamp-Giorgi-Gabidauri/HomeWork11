package com.gabo.rvwithtwoitems.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gabo.rvwithtwoitems.database.dao.PostDao
import com.gabo.rvwithtwoitems.database.entity.Post

@Database(entities = [Post::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): PostDao
}