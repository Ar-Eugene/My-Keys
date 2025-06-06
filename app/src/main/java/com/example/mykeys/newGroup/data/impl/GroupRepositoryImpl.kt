package com.example.mykeys.newGroup.data.impl

import com.example.mykeys.newGroup.data.db.dao.GroupDao
import com.example.mykeys.newGroup.data.db.converter.GroupDbConverter
import com.example.mykeys.newGroup.domain.models.GroupModel
import com.example.mykeys.newGroup.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val groupDbConverter: GroupDbConverter
) : GroupRepository {

    override fun getGroup(): Flow<List<GroupModel>> {
        return groupDao.getGroup().map { entities ->
            groupDbConverter.mapEntityListToModelList(entities)
        }
    }

    override suspend fun createGroup(group: GroupModel) {
        groupDao.insertGroup(groupDbConverter.mapModelToEntity(group))
    }

    override suspend fun deleteGroupById(id: Int) {
        groupDao.deleteGroupById(id)
    }

    override suspend fun updateGroup(group: GroupModel) {
        groupDao.updateGroup(groupDbConverter.mapModelToEntity(group))
    }

    override suspend fun isGroupNameExists(name: String, currentId: Int): Boolean {
        return groupDao.isGroupNameExists(name, currentId) > 0
    }

}