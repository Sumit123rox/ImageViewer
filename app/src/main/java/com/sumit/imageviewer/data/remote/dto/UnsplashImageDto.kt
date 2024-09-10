package com.sumit.imageviewer.data.remote.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class UnsplashImageDto(
    val blur_hash: String? = null,
    val color: String? = null,
    val created_at: String? = null,
    val current_user_collections: List<CurrentUserCollection?>? = null,
    val description: String? = null,
    val height: Int? = null,
    val id: String,
    val liked_by_user: Boolean? = null,
    val likes: Int? = null,
    val links: Links? = null,
    val updated_at: String? = null,
    val urls: Urls? = null,
    val user: User? = null,
    val width: Int? = null
) {
    @Serializable
    data class CurrentUserCollection(
        val cover_photo: String? = null,
        val id: Int? = null,
        val last_collected_at: String? = null,
        val published_at: String? = null,
        val title: String? = null,
        val updated_at: String? = null,
        val user: User? = null
    )

    @Serializable
    data class Links(
        val download: String? = null,
        val download_location: String? = null,
        val html: String? = null,
        val self: String? = null
    )

    @Serializable
    data class Urls(
        val full: String? = null,
        val raw: String? = null,
        val regular: String? = null,
        val small: String? = null,
        val thumb: String? = null
    )

    @Serializable
    data class User(
        val bio: String? = null,
        val id: String? = null,
        val instagram_username: String? = null,
        val links: Links? = null,
        val location: String? = null,
        val name: String? = null,
        val portfolio_url: String? = null,
        val profile_image: ProfileImage? = null,
        val total_collections: Int? = null,
        val total_likes: Int? = null,
        val total_photos: Int? = null,
        val twitter_username: String? = null,
        val username: String? = null
    ) {
        @Serializable
        data class Links(
            val html: String? = null,
            val likes: String? = null,
            val photos: String? = null,
            val portfolio: String? = null,
            val self: String? = null
        )

        @Serializable
        data class ProfileImage(
            val large: String? = null,
            val medium: String? = null,
            val small: String? = null
        )
    }
}