package com.example.mykeys.newGroup.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="group_room" )
data class GroupEntity(
    @PrimaryKey
    val id:Int = 0,
    val imageGroup:Int,
    val nameGroup:String
)
