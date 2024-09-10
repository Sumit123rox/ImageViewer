package com.sumit.imageviewer.di

import android.content.Context
import androidx.room.Room
import com.sumit.imageviewer.data.local.imageviwerDatabase
import com.sumit.imageviewer.data.repository.AndroidImageDownloader
import com.sumit.imageviewer.data.repository.ImageRepositoryImpl
import com.sumit.imageviewer.data.repository.NetworkConnectivityObserverImpl
import com.sumit.imageviewer.domain.repository.Downloader
import com.sumit.imageviewer.domain.repository.ImageRepository
import com.sumit.imageviewer.domain.repository.NetworkConnectivityObserver
import com.sumit.imageviewer.utils.Constants.IMAGE_VISTA_DATABASE
import com.sumit.imageviewer.utils.Constants.TIME_OUT
import com.sumit.imageviewer.utils.Constants.TIME_OUT_LONG
import com.sumit.imageviewer.utils.Constants.UNSPLASH_API_KEY
import com.sumit.imageviewer.utils.loge
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideimageviwerDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context = context,
        klass = imageviwerDatabase::class.java,
        name = IMAGE_VISTA_DATABASE
    ).build()

    @Provides
    @Singleton
    fun provideKtorHttpClient(): HttpClient = HttpClient(Android) {

        val json = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }

        install(ContentNegotiation) {
            json(json = json)
            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIME_OUT_LONG
            connectTimeoutMillis = TIME_OUT_LONG
            socketTimeoutMillis = TIME_OUT_LONG
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    loge { message }
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                loge { "${response.status.value}" }
            }
        }

        install(DefaultRequest) {
            header(
                HttpHeaders.Authorization,
                "Client-ID $UNSPLASH_API_KEY"
            )
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    fun provideAndroidImageDownloader(
        @ApplicationContext context: Context,
    ): Downloader {
        return AndroidImageDownloader(context)
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        client: HttpClient,
        imageviwerDatabase: imageviwerDatabase,
    ): ImageRepository {
        return ImageRepositoryImpl(client, imageviwerDatabase)
    }

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)


    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context,
        scope: CoroutineScope,
    ): NetworkConnectivityObserver =
        NetworkConnectivityObserverImpl(context = context, scope = scope)

}