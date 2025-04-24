package com.example.mykeys.di

import android.content.Context
import com.example.mykeys.password.PasswordManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePasswordManager(@ApplicationContext context: Context): PasswordManager {
        return PasswordManager(context)
    }
}