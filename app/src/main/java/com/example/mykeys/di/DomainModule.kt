package com.example.mykeys.di

import com.example.mykeys.newGroup.domain.interactor.GroupInteractor
import com.example.mykeys.newGroup.domain.interactor.impl.GroupInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindGroupInteractor(
        groupInteractorImpl: GroupInteractorImpl
    ): GroupInteractor

}