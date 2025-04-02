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
//        val entity = groupDbConverter.mapModelToEntity(group)
//        groupDao.insertGroup(entity)
    }

    override suspend fun updateGroupPositions(groups: List<GroupModel>){
        val entities = groups.map { groupDbConverter.mapModelToEntity(it) }
        groupDao.updatePositions(entities)
    }

    override suspend fun deleteGroupById(id: Int){
        groupDao.deleteGroupById(id)
    }

}