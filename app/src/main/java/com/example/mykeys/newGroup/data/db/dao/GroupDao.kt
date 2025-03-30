package com.example.mykeys.newGroup.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mykeys.newGroup.data.db.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM group_room")
    fun getGroup(): Flow<List<GroupEntity>>

    @Insert()
    suspend fun insertGroup(group: GroupEntity)

    @Transaction
    suspend fun updatePositions(groups: List<GroupEntity>) {
        for (group in groups) {
            updateGroup(group)
        }
    }
    @Query("DELETE FROM group_room WHERE id = :id")
    suspend fun deleteGroupById(id: Int)

    @Update
    suspend fun updateGroup(group: GroupEntity)
}