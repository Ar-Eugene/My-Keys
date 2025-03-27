package com.example.mykeys.newGroup.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mykeys.newGroup.data.db.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM group_room")
    fun getGroup(): Flow<List<GroupEntity>>

    @Insert()
    suspend fun insertGroup(group: GroupEntity)
}