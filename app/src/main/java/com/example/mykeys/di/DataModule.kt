package com.example.mykeys.di

import android.content.Context
import androidx.room.Room
import com.example.mykeys.newGroup.data.db.GroupDatabase
import com.example.mykeys.newGroup.data.db.dao.GroupDao
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
    fun room(@ApplicationContext context: Context): GroupDatabase {
        return Room.databaseBuilder(
            context,
            GroupDatabase::class.java,
            "group_database"
        ).build()
    }

    @Provides
    @Singleton
    fun getDao(database: GroupDatabase): GroupDao {
        return database.groupDao()
    }
}