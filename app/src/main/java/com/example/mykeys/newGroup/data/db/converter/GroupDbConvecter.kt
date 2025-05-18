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
            emailGroup = groupEntity.emailGroup,
            passwordGroup = groupEntity.passwordGroup,
            loginGroup = groupEntity.loginGroup,
            position = groupEntity.position
        )
    }

    fun mapModelToEntity(groupModel: GroupModel): GroupEntity {
        return GroupEntity(
            id = groupModel.id,
            imageGroup = groupModel.imageGroup?.toString(),
            nameGroup = groupModel.nameGroup,
            emailGroup = groupModel.emailGroup,
            passwordGroup = groupModel.passwordGroup,
            loginGroup = groupModel.loginGroup,
            position = groupModel.position
        )
    }

    fun mapEntityListToModelList(entities: List<GroupEntity>): List<GroupModel> {
        return entities.map { mapEntityToModel(it) }
    }

}