package com.example.mykeys.newGroup.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mykeys.newGroup.data.db.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM group_room ORDER BY position ASC")
    fun getGroup(): Flow<List<GroupEntity>>

    @Insert()
    suspend fun insertGroup(group: GroupEntity)

    @Query("DELETE FROM group_room WHERE id = :id")
    suspend fun deleteGroupById(id: Int)

    @Update()
    suspend fun updateGroup(group: GroupEntity)

    @Query("SELECT COUNT(*) FROM group_room WHERE LOWER(nameGroup) = LOWER(:name) AND id != :currentId")
    suspend fun isGroupNameExists(name: String, currentId: Int = 0): Int

}