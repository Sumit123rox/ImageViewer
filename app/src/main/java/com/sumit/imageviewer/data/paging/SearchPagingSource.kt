package com.sumit.imageviewer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sumit.imageviewer.data.mappers.toDomainModelList
import com.sumit.imageviewer.data.remote.dto.UnsplashImagesSearchResponse
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.utils.Constants.BASE_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class SearchPagingSource(
    private val query: String,
    private val client: HttpClient
) : PagingSource<Int, UnsplashImage>() {

    companion object {
        const val START_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashImage>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImage> =
        try {
            val currentPage = params.key ?: START_PAGE_INDEX
            val response =
                client.get("$BASE_URL/search/photos?query=$query&page=$currentPage&per_page=${params.loadSize}")
                    .body<UnsplashImagesSearchResponse>()

            val endOfPaginationReached = response.results.isEmpty()

            LoadResult.Page(
                data = response.results.toDomainModelList() ?: emptyList(),
                prevKey = if (currentPage == START_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
}
