package com.example.mykeys.main.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class MainFragmentViewModel @Inject constructor(
    private val groupInteractor: GroupInteractor
) : ViewModel() {

    // LiveData для хранения текста поиска
    private val _searchText = MutableLiveData<String>()
    var searchText: LiveData<String> = _searchText

    // StateFlow для хранения списка групп
    private val _groups = MutableStateFlow<List<GroupModel>>(emptyList())
    val groups: StateFlow<List<GroupModel>> = _groups

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            groupInteractor.getGroup().collect { groupList ->
                _groups.value = groupList
            }
        }
    }

    // Функция для обновления текста поиска
    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    // Функция для удаления раздела
    fun deleteGroup(group: GroupModel) {
        viewModelScope.launch {
            groupInteractor.deleteGroupById(group.id)

        }
    }

}