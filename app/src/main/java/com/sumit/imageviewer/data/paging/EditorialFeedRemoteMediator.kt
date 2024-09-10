package com.sumit.imageviewer.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sumit.imageviewer.data.local.imageviwerDatabase
import com.sumit.imageviewer.data.local.entity.UnsplashImageEntity
import com.sumit.imageviewer.data.local.entity.UnsplashRemoteKeys
import com.sumit.imageviewer.data.mappers.toEntityList
import com.sumit.imageviewer.data.remote.dto.UnsplashImageDto
import com.sumit.imageviewer.utils.Constants.BASE_URL
import com.sumit.imageviewer.utils.Constants.ITEMS_PER_PAGE
import com.sumit.imageviewer.utils.loge
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

@OptIn(ExperimentalPagingApi::class)
class EditorialFeedRemoteMediator(
    private val client: HttpClient,
    private val database: imageviwerDatabase
) : RemoteMediator<Int, UnsplashImageEntity>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    private val editorialFeedDao = database.editorialFeedDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UnsplashImageEntity>): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: STARTING_PAGE_INDEX
                }

                PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstTime(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                    prevPage
                }

                APPEND -> {
                    val remoteKeys = getRemoteKeyForLastTime(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextPage
                }
            }

            loge { "CurrentPage: $currentPage" }

            val response = client.get("$BASE_URL/photos?page=$currentPage&per_page=$ITEMS_PER_PAGE")
                .body<List<UnsplashImageDto>>()

            val endOfPaginationReached = response.isEmpty()

            loge { "endOfPaginationReached: $endOfPaginationReached" }

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == REFRESH) {
                    editorialFeedDao.deleteAllEditorialFeedImages()
                    editorialFeedDao.deleteAllRemoteKeys()
                }

                val remoteKeys = response.map { unsplashImageDto ->
                    UnsplashRemoteKeys(
                        id = unsplashImageDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                editorialFeedDao.insertAllRemoteKeys(remoteKeys)
                editorialFeedDao.insertEditorialFeedImages(response.toEntityList())
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UnsplashImageEntity>
    ): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                editorialFeedDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstTime(
        state: PagingState<Int, UnsplashImageEntity>
    ): UnsplashRemoteKeys? =
        state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            editorialFeedDao.getRemoteKeys(it.id)
        }

    private suspend fun getRemoteKeyForLastTime(
        state: PagingState<Int, UnsplashImageEntity>
    ): UnsplashRemoteKeys? =
        state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            editorialFeedDao.getRemoteKeys(it.id)
        }
}