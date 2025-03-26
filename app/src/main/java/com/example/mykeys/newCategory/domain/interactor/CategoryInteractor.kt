package com.example.mykeys.newCategory.domain.interactor

import com.example.mykeys.newCategory.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryInteractor {

    fun getCategory(): Flow<List<CategoryModel>>

    suspend fun createCategory(category: CategoryModel)
}