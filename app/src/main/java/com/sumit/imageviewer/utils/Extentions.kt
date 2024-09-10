package com.sumit.imageviewer.utils

import android.content.Context
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import timber.log.Timber

fun loge(message: () -> String) {
    Timber.Forest.e(message())
}

fun Context.toast(message: () -> String) {
    Toast.makeText(this, message(), Toast.LENGTH_SHORT).show()
}

fun WindowInsetsControllerCompat.toggleStatusBar(show: Boolean) {
    if (show) show(WindowInsetsCompat.Type.systemBars())
    else hide(WindowInsetsCompat.Type.systemBars())
}

