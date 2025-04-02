package com.example.mykeys.newGroup.domain.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupModel(
    val id:Int = 0,
    val imageGroup: Uri? = null,
    val nameGroup:String,
    val position: Int = 0
): Parcelable
