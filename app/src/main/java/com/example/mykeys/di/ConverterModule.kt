package com.example.mykeys.di

import com.example.mykeys.newCategory.data.db.сonverter.CategoryDbConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object ConverterModule {

    @Provides

    fun provideCategoryConverter(): CategoryDbConverter {
        return CategoryDbConverter()
    }


}