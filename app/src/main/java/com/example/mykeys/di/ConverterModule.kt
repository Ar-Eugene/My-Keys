package com.example.mykeys.di

import com.example.mykeys.newGroup.data.db.converter.GroupDbConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object ConverterModule {

    @Provides
    fun provideGroupConverter(): GroupDbConverter {
        return GroupDbConverter()
    }


}