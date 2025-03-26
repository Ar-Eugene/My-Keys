package com.example.mykeys.newCategory.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mykeys.newCategory.domain.interactor.CategoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewCategoryViewModel @Inject constructor(
    interactor: CategoryInteractor)
    : ViewModel() {
}