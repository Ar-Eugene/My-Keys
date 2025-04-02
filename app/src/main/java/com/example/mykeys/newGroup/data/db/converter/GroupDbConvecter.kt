package com.example.mykeys.newGroup.data.db.converter

import android.net.Uri
import com.example.mykeys.newGroup.data.db.entity.GroupEntity
import com.example.mykeys.newGroup.domain.models.GroupModel

class GroupDbConverter {

    fun mapEntityToModel(groupEntity: GroupEntity): GroupModel {
        return GroupModel(
            id = groupEntity.id,
            imageGroup = groupEntity.imageGroup?.let { Uri.parse(it) },
            nameGroup = groupEntity.nameGroup,
            position = groupEntity.position
        )
    }

    fun mapModelToEntity(groupModel: GroupModel): GroupEntity {
        return GroupEntity(
            id = groupModel.id,
            imageGroup = groupModel.imageGroup?.toString(),
            nameGroup = groupModel.nameGroup,
            position = groupModel.position
        )
    }


    fun mapEntityListToModelList(entities: List<GroupEntity>): List<GroupModel> {
        return entities.map { mapEntityToModel(it) }
    }

    fun mapModelListToEntityList(models: List<GroupModel>): List<GroupEntity> {
        return models.map { mapModelToEntity(it) }
    }
}