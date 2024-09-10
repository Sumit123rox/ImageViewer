package com.sumit.imageviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sumit.imageviewer.data.local.entity.FavoriteImageEntity
import com.sumit.imageviewer.data.local.entity.UnsplashImageEntity
import com.sumit.imageviewer.data.local.entity.UnsplashRemoteKeys

@Database(
    entities = [FavoriteImageEntity::class, UnsplashImageEntity::class, UnsplashRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class imageviwerDatabase : RoomDatabase() {
    abstract fun favoriteImageDao(): FavoriteImageDao
    abstract fun editorialFeedDao(): EditorialFeedDao
}