package com.example.mykeys.newCategory.domain.interactor.impl

import com.example.mykeys.newCategory.domain.interactor.CategoryInteractor
import com.example.mykeys.newCategory.domain.models.CategoryModel
import com.example.mykeys.newCategory.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryInteractorImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) :
    CategoryInteractor {

    override fun getCategory(): Flow<List<CategoryModel>> {
        return categoryRepository.getCategory()
    }

    override suspend fun createCategory(category: CategoryModel) {
        return categoryRepository.createCategory(category)
    }

}