package com.example.mykeys.main.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainFragmentViewModel():ViewModel() {

    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    var navigationEvent:LiveData<NavigationEvent> = _navigationEvent

    // Функция для обработки нажатия кнопки
    fun onApplyButtonClick(){
        _navigationEvent.value = NavigationEvent.NavigationToNewGroupFragment
    }

    //Sealed class для событий навигации
    sealed class NavigationEvent{
        object NavigationToNewGroupFragment:NavigationEvent()
    }
}