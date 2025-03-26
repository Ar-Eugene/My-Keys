package com.example.mykeys.newCategory.data.db.сonverter

import com.example.mykeys.newCategory.data.db.entity.CategoryEntity
import com.example.mykeys.newCategory.domain.models.CategoryModel

class CategoryDbConverter {

    fun mapEntityToModel(categoryEntity: CategoryEntity): CategoryModel {
        return CategoryModel(
            id = categoryEntity.id,
            imageCategory = categoryEntity.imageCategory,
            nameCategory = categoryEntity.nameCategory
        )
    }

    fun mapModelToEntity(categoryModel: CategoryModel): CategoryEntity {
        return CategoryEntity(
            id = categoryModel.id,
            imageCategory = categoryModel.imageCategory,
            nameCategory = categoryModel.nameCategory
        )
    }

    fun mapEntityListToModelList(entities: List<CategoryEntity>): List<CategoryModel> {
        return entities.map { mapEntityToModel(it) }
    }

    fun mapModelListToEntityList(models: List<CategoryModel>): List<CategoryEntity> {
        return models.map { mapModelToEntity(it) }
    }
}