package com.example.mykeys.newCategory.data.impl

import com.example.mykeys.newCategory.data.db.dao.CategoryDao
import com.example.mykeys.newCategory.data.db.сonverter.CategoryDbConverter
import com.example.mykeys.newCategory.domain.models.CategoryModel
import com.example.mykeys.newCategory.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryDbConverter: CategoryDbConverter
) : CategoryRepository {

    override fun getCategory(): Flow<List<CategoryModel>> {
        return categoryDao.getCategory().map { entities ->
            categoryDbConverter.mapEntityListToModelList(entities)
        }
    }

    override suspend fun createCategory(category: CategoryModel) {
        categoryDao.insertCategory(categoryDbConverter.mapModelToEntity(category))
    }

}