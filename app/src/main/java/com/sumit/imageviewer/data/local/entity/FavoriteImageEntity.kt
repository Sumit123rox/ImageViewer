package com.sumit.imageviewer.data.local.entity

import androidx.room.Entity
import com.sumit.imageviewer.utils.Constants.FAVORITE_IMAGE_TABLE

@Entity(tableName = FAVORITE_IMAGE_TABLE, primaryKeys = ["id"])
data class FavoriteImageEntity(
    val id: String,
    val imageUrlSmall: String?,
    val imageUrlRegular: String?,
    val imageUrlRaw: String?,
    val photographerName: String?,
    val photographerUsername: String?,
    val photographerProfileImgUrl: String?,
    val photographerProfileLink: String?,
    val width: Int?,
    val height: Int?,
    val description: String?,
)
