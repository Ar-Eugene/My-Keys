package com.example.mykeys.newGroup.domain.repository

import com.example.mykeys.newGroup.domain.models.GroupModel
import kotlinx.coroutines.flow.Flow

interface GroupRepository {

    fun getGroup(): Flow<List<GroupModel>>

    suspend fun createGroup(group: GroupModel)

    suspend fun updateGroupPositions(groups: List<GroupModel>)

    suspend fun deleteGroupById(id: Int)
}