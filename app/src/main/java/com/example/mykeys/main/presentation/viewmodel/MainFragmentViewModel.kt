package com.example.mykeys.main.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainFragmentViewModel():ViewModel() {

    // LiveData для хранения навигации
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    var navigationEvent:LiveData<NavigationEvent> = _navigationEvent

    // LiveData для хранения текста поиска
    private val _searchText = MutableLiveData<String>()
    var searchText:LiveData<String> = _searchText

    // Функция для обновления текста поиска
    fun updateSearchText(text:String){
        _searchText.value = text
    }

    // Функция для обработки нажатия кнопки
    fun onApplyButtonClick(){
        _navigationEvent.value = NavigationEvent.NavigationToNewGroupFragment
    }

    //Sealed class для событий навигации
    sealed class NavigationEvent{
        object NavigationToNewGroupFragment:NavigationEvent()
    }
}