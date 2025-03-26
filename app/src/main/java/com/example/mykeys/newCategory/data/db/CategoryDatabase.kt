package com.example.mykeys.newCategory.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mykeys.newCategory.data.db.dao.CategoryDao
import com.example.mykeys.newCategory.data.db.entity.CategoryEntity

@Database(version = 1, entities = [CategoryEntity::class])
abstract class CategoryDatabase:RoomDatabase() {
    abstract fun categoryDao():CategoryDao
}