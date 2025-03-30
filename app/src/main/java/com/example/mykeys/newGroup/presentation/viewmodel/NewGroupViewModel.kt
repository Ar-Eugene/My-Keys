package com.example.mykeys.newGroup.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykeys.newGroup.domain.interactor.GroupInteractor
import com.example.mykeys.newGroup.domain.models.GroupModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewGroupViewModel @Inject constructor(
    private val groupInteractor: GroupInteractor
) : ViewModel() {

    private val _category = MutableStateFlow<List<GroupModel>>(emptyList())

    // Для хранения URI изображения
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    // Для хранения имени группы
    private val _groupName = MutableStateFlow("")

    init {
        viewModelScope.launch {
            groupInteractor.getGroup().collect { categories ->
                _category.value = categories
            }
        }
    }

    // Функция для установки URI изображения
    fun setImageGroup(uri: Uri) {
        _selectedImageUri.value = uri
    }

    // Функция для установки имени группы
    fun setGroupName(name: String) {
        _groupName.value = name
    }

    // Функция для создания группы
    fun createGroup() {
        val imageGroup = _selectedImageUri.value?.toString()
        val name = _groupName.value

        if (name.isNotBlank()) {
            val newGroup = GroupModel(
                imageGroup = imageGroup,
                nameGroup = name
            )

            viewModelScope.launch {
                groupInteractor.createGroup(newGroup)
            }
        }
    }
}
