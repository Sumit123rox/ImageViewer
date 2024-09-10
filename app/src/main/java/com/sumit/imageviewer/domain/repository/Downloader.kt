package com.sumit.imageviewer.domain.repository

interface Downloader {
    fun downloadImage(url: String, fileName: String?)
}