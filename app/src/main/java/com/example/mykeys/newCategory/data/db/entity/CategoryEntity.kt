package com.example.mykeys.newCategory.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="category_room" )
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val imageCategory:Int,
    val nameCategory:String
)
