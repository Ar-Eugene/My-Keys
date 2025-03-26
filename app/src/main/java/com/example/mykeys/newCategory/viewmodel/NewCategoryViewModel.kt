package com.example.mykeys.newCategory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykeys.newCategory.domain.interactor.CategoryInteractor
import com.example.mykeys.newCategory.domain.models.CategoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCategoryViewModel @Inject constructor(
    private val categoryInteractor: CategoryInteractor
) : ViewModel() {

    private val _category = MutableStateFlow<List<CategoryModel>>(emptyList())
    var category: StateFlow<List<CategoryModel>> = _category

    init {
        viewModelScope.launch {
            categoryInteractor.getCategory().collect { categories ->
                _category.value=categories
            }
        }
    }

    fun addCategory(categoryModel: CategoryModel){
        viewModelScope.launch {
            categoryInteractor.createCategory(categoryModel)
        }
    }
}