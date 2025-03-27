package com.example.mykeys.newGroup.presentation.viewmodel

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
class NewGroupViewModel @Inject constructor(
    private val groupInteractor: GroupInteractor
) : ViewModel() {

    private val _category = MutableStateFlow<List<GroupModel>>(emptyList())
    var category: StateFlow<List<GroupModel>> = _category

    // LiveData для хранения навигации
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    var navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    init {
        viewModelScope.launch {
            groupInteractor.getGroup().collect { categories ->
                _category.value=categories
            }
        }
    }

    // Функция для создания категории
    fun addCategory(groupModel: GroupModel){
        viewModelScope.launch {
            groupInteractor.createGroup(groupModel)
        }
    }

    // Функция для обработки нажатия кнопки
    fun onApplyButtonClick(){
        _navigationEvent.value = NavigationEvent.NavigationToMainFragment
    }

    //Sealed class для событий навигации
    sealed class NavigationEvent{
        object NavigationToMainFragment: NavigationEvent()
    }
}

