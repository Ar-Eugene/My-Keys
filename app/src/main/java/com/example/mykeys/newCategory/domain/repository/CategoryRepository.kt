package com.example.mykeys.newCategory.domain.repository

import com.example.mykeys.newCategory.data.db.entity.CategoryEntity
import com.example.mykeys.newCategory.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getCategory(): Flow<List<CategoryModel>>

    suspend fun createCategory(category: CategoryModel)
}