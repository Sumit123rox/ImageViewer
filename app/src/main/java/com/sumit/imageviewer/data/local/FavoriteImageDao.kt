package com.sumit.imageviewer.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sumit.imageviewer.data.local.entity.FavoriteImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteImageDao {

    @Query("SELECT * FROM favorite_images_table")
    fun getAllFavoriteImages(): PagingSource<Int, FavoriteImageEntity>

    @Upsert
    suspend fun insertFavoriteImage(image: FavoriteImageEntity)

    @Delete
    suspend fun deleteFavoriteImage(image: FavoriteImageEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_images_table WHERE id = :id)")
    suspend fun isImageFavorite(id: String): Boolean

    @Query("SELECT id FROM favorite_images_table")
    fun getFavoriteImageIds(): Flow<List<String>>
}