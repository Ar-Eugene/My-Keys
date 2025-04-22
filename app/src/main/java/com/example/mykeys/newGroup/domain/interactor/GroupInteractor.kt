package com.example.mykeys.newGroup.domain.interactor

import com.example.mykeys.newGroup.domain.models.GroupModel
import kotlinx.coroutines.flow.Flow

interface GroupInteractor {

    fun getGroup(): Flow<List<GroupModel>>

    suspend fun createGroup(group: GroupModel)

    suspend fun deleteGroupById(id: Int)

    suspend fun updateGroup (group: GroupModel)
}