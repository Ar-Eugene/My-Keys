package com.example.mykeys.newGroup.data.db.converter

import com.example.mykeys.newGroup.data.db.entity.GroupEntity
import com.example.mykeys.newGroup.domain.models.GroupModel

class GroupDbConverter {

    fun mapEntityToModel(groupEntity: GroupEntity): GroupModel {
        return GroupModel(
            id = groupEntity.id,
            imageGroup = groupEntity.imageGroup,
            nameGroup = groupEntity.nameGroup
        )
    }

    fun mapModelToEntity(groupModel: GroupModel): GroupEntity {
        return GroupEntity(
            id = groupModel.id,
            imageGroup = groupModel.imageGroup,
            nameGroup = groupModel.nameGroup
        )
    }


    fun mapEntityListToModelList(entities: List<GroupEntity>): List<GroupModel> {
        return entities.map { mapEntityToModel(it) }
    }

    fun mapModelListToEntityList(models: List<GroupModel>): List<GroupEntity> {
        return models.map { mapModelToEntity(it) }
    }
}