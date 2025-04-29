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
    val groupName: StateFlow<String> = _groupName

    // Для хранения email группы
    private val _emailName = MutableStateFlow("")
    val emailName: StateFlow<String> = _emailName

    // Для хранения пароля группы
    private val _passwordName = MutableStateFlow("")
    val passwordName: StateFlow<String> = _passwordName

    // Для хранения логин группы
    private val _loginName = MutableStateFlow("")
    val loginName: StateFlow<String> = _loginName

    // Для хранения ID группы при редактировании
    private var currentGroupId: Int? = null

    // Флаг режима редактирования
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    init {
        viewModelScope.launch {
            groupInteractor.getGroup().collect { categories ->
                _category.value = categories
            }
        }
    }

    // Метод для загрузки данных группы для редактирования
    fun loadGroupForEdit(groupModel: GroupModel) {
        currentGroupId = groupModel.id
        _selectedImageUri.value = groupModel.imageGroup
        _groupName.value = groupModel.nameGroup ?: ""
        _emailName.value = groupModel.emailGroup ?: ""
        _passwordName.value = groupModel.passwordGroup ?: ""
        _loginName.value = groupModel.loginGroup ?: ""
        _isEditMode.value = true
    }

    // Функция для установки URI изображения
    fun setImageGroup(uri: Uri) {
        _selectedImageUri.value = uri
    }

    // Функция для установки имени группы
    fun setGroupName(name: String) {
        _groupName.value = name
    }

    // Функция для установки email группы
    fun setEmailName(email: String) {
        _emailName.value = email
    }

    // Функция для установки Password группы
    fun setPasswordName(password: String) {
        _passwordName.value = password
    }

    // Функция для установки Login группы
    fun setLoginName(login: String) {
        _loginName.value = login
    }

    // Функция для создания и редактирования группы
    fun createOrSaveGroup(): GroupModel? {
        val imageGroup = _selectedImageUri.value
        val name = _groupName.value
        val emailName = _emailName.value
        val passwordName = _passwordName.value
        val loginName = _loginName.value
        if (name.isNotBlank()) {
            val group = GroupModel(
                id = currentGroupId ?: 0,
                imageGroup = imageGroup,
                nameGroup = name,
                emailGroup = emailName,
                passwordGroup = passwordName,
                loginGroup = loginName,
                position = 0
            )
            viewModelScope.launch {
                if (_isEditMode.value) {
                    groupInteractor.updateGroup(group)
                } else {
                    groupInteractor.createGroup(group)
                }
            }
            return group
        }
        return null
    }

    // Сброс режима редактирования
    fun resetState() {
        currentGroupId = null
        _selectedImageUri.value = null
        _groupName.value = ""
        _emailName.value = ""
        _passwordName.value = ""
        _loginName.value = ""
        _isEditMode.value = false
    }
}
