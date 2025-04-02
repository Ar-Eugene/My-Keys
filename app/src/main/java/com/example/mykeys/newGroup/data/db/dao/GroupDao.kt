package com.example.mykeys.newGroup.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

//    @Transaction
//    suspend fun updatePositions(groups: List<GroupEntity>) {
//        for (group in groups) {
//            Log.d("GroupDao", "Updating group: ${group.id} -> position: ${group.position}")
//
//            updateGroup(group)
//        }
//    }
    @Query("DELETE FROM group_room WHERE id = :id")
    suspend fun deleteGroupById(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePositions(groups: List<GroupEntity>)
}