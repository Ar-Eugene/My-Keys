package com.example.mykeys.di

import android.content.Context
import androidx.room.Room
import com.example.mykeys.newCategory.data.db.CategoryDatabase
import com.example.mykeys.newCategory.data.db.dao.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun room(@ApplicationContext context: Context):CategoryDatabase{
        return Room.databaseBuilder(
            context,
            CategoryDatabase::class.java,
            "category_database"
        ).build()
    }
    @Provides
    @Singleton
    fun getDao(database:CategoryDatabase):CategoryDao{
        return database.categoryDao()
    }
}