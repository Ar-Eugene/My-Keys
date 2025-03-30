package com.example.mykeys.main.presentation.viewmodel

import android.util.Log
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
import java.util.Collections
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


    fun onItemMove(fromPosition: Int, toPosition: Int) {
        val currentList = _groups.value.toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(currentList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(currentList, i, i - 1)
            }
        }

        // Обновляем позиции в моделях
        val updatedList = currentList.mapIndexed { index, group ->
            group.copy(position = index)
        }

        _groups.value = updatedList
        viewModelScope.launch {
            groupInteractor.updateGroupPositions(updatedList)
        }
    }

    fun deleteGroup(id: Int) {
        viewModelScope.launch {
            try {
                groupInteractor.deleteGroupById(id)
                // После удаления обновляем позиции оставшихся элементов
                val updatedList = _groups.value.mapIndexed { index, group ->
                    group.copy(position = index)
                }
                groupInteractor.updateGroupPositions(updatedList)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при удалении группы", e)
            }
        }
    }

}