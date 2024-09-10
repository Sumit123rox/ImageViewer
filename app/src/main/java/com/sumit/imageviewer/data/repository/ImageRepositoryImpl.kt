package com.sumit.imageviewer.data.repository

import androidx.paging.*
import com.sumit.imageviewer.data.local.imageviwerDatabase
import com.sumit.imageviewer.data.mappers.toDomainModel
import com.sumit.imageviewer.data.mappers.toFavoriteImageEntity
import com.sumit.imageviewer.data.paging.EditorialFeedRemoteMediator
import com.sumit.imageviewer.data.paging.SearchPagingSource
import com.sumit.imageviewer.data.remote.dto.UnsplashImageDto
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.domain.repository.ImageRepository
import com.sumit.imageviewer.utils.Constants.BASE_URL
import com.sumit.imageviewer.utils.Constants.ITEMS_PER_PAGE
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageRepositoryImpl(
    private val client: HttpClient,
    private val database: imageviwerDatabase,
) : ImageRepository {

    private val favoriteImageDao = database.favoriteImageDao()
    private val editorialImageDao = database.editorialFeedDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> =
        Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = EditorialFeedRemoteMediator(client = client, database = database),
            pagingSourceFactory = { editorialImageDao.getAllEditorialFeedImages() }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }

    override suspend fun getPhotoById(id: String): UnsplashImage =
        client.get("$BASE_URL/photos/$id").body<UnsplashImageDto>().toDomainModel()

    override fun searchImage(query: String): Flow<PagingData<UnsplashImage>> =
        Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = { SearchPagingSource(query = query, client = client) }
        ).flow

    override fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE),
        pagingSourceFactory = { favoriteImageDao.getAllFavoriteImages() }
    ).flow
        .map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }

    override suspend fun toggleFavoriteStatus(image: UnsplashImage) {
        val isFavorite = favoriteImageDao.isImageFavorite(image.id)
        val favoriteImageEntity = image.toFavoriteImageEntity()
        if (isFavorite) {
            favoriteImageDao.deleteFavoriteImage(favoriteImageEntity)
        } else {
            favoriteImageDao.insertFavoriteImage(favoriteImageEntity)
        }
    }

    override fun getFavoriteImageIds(): Flow<List<String>> =
        favoriteImageDao.getFavoriteImageIds()

}