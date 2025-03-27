package com.example.mykeys.newGroup.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mykeys.newGroup.data.db.dao.GroupDao
import com.example.mykeys.newGroup.data.db.entity.GroupEntity

@Database(version = 1, entities = [GroupEntity::class])
abstract class GroupDatabase:RoomDatabase() {
    abstract fun groupDao(): GroupDao
}