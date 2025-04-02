package com.example.mykeys.newGroup.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="group_room" )
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val imageGroup:String? = null,
    val nameGroup:String,
    val position: Int = 0
)
