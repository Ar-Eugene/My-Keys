package com.example.mykeys.newCategory.domain.models

import androidx.room.PrimaryKey

data class CategoryModel(
    val id:Int = 0,
    val imageCategory:Int,
    val nameCategory:String
)
