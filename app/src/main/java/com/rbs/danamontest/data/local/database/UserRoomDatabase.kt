package com.rbs.danamontest.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rbs.danamontest.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class], version = 4, exportSchema = false
)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao
}