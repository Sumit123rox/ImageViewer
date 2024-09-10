package com.sumit.imageviewer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImagesSearchResponse(
    val results: List<UnsplashImageDto>,
    val total: Int,
    val total_pages: Int
)