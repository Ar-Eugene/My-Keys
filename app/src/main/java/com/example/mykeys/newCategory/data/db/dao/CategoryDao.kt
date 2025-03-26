package com.example.mykeys.newCategory.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mykeys.newCategory.data.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category_room")
    fun getCategory(): Flow<List<CategoryEntity>>

    @Insert()
    fun insertCategory(category:CategoryEntity)
}