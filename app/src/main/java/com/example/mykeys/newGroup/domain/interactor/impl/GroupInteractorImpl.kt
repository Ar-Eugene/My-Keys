package com.example.mykeys.newGroup.domain.interactor.impl

import com.example.mykeys.newGroup.domain.interactor.GroupInteractor
import com.example.mykeys.newGroup.domain.models.GroupModel
import com.example.mykeys.newGroup.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupInteractorImpl @Inject constructor(
    private val groupRepository: GroupRepository
) :
    GroupInteractor {

    override fun getGroup(): Flow<List<GroupModel>> {
        return groupRepository.getGroup()
    }

    override suspend fun createGroup(group: GroupModel) {
        return groupRepository.createGroup(group)
    }

    override suspend fun deleteGroupById(id: Int){
        return groupRepository.deleteGroupById(id)
    }

}