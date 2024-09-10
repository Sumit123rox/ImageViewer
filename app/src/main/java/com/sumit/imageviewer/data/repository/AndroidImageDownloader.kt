package com.sumit.imageviewer.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.sumit.imageviewer.domain.repository.Downloader
import io.ktor.utils.io.printStack
import java.io.File

class AndroidImageDownloader(
    context: Context
) : Downloader {

    private val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override fun downloadImage(url: String, fileName: String?) {
        try {
            val title = fileName ?: "New Image"
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("Image/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(title)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + title + ".jpg"
                )
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStack()
        }
    }
}