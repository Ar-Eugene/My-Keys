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
    private val _allGroups = MutableStateFlow<List<GroupModel>>(emptyList())
    val allGroups: StateFlow<List<GroupModel>> = _allGroups

    private val _filterGroups = MutableLiveData<List<GroupModel>>(emptyList())
    val filterGroups:LiveData<List<GroupModel>> = _filterGroups


    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            groupInteractor.getGroup().collect { groupList ->
                _allGroups.value = groupList
                filterGroups(_searchText.value ?: "")

            }
        }
    }

    // Функция для обновления текста поиска
    fun updateSearchText(text: String) {
        _searchText.value = text
        filterGroups(text)
    }

    private fun filterGroups (text: String){
        if(text.isEmpty()){
            // если запрос пустой все группы
            _filterGroups.value = _allGroups.value
        }else{
            _filterGroups.value = _allGroups.value.filter { group ->
                group.nameGroup.contains(text, ignoreCase = true)
            }
        }
    }

    // Функция для удаления раздела
    fun deleteGroup(group: GroupModel) {
        viewModelScope.launch {
            groupInteractor.deleteGroupById(group.id)

        }
    }

}