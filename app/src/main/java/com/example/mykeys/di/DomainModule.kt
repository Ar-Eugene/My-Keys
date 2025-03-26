package com.example.mykeys.di

import com.example.mykeys.newCategory.domain.interactor.CategoryInteractor
import com.example.mykeys.newCategory.domain.interactor.impl.CategoryInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindCategoryInteractor(
        categoryInteractorImpl: CategoryInteractorImpl
    ): CategoryInteractor

}