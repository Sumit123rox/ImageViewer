package com.sumit.imageviewer.presentation.fullImage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.domain.repository.Downloader
import com.sumit.imageviewer.domain.repository.ImageRepository
import com.sumit.imageviewer.presentation.navigation.Routes.FullImageScreen
import com.sumit.imageviewer.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageId = savedStateHandle.toRoute<FullImageScreen>().imageId

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    var image by mutableStateOf<UnsplashImage?>(null)
        private set

    init {
        getImage()
    }

    private fun getImage() {
        viewModelScope.launch {
            try {
                val result = repository.getPhotoById(imageId)
                image = result
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(message = "No Internet Connection. Please check your Internet Connection.")
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(message = "Something went wrong: ${e.message}")
                )
            }
        }
    }

    fun downloadImage(url: String, fileName: String?) {
        viewModelScope.launch {
            try {
                downloader.downloadImage(url = url, fileName = fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(message = "Something went wrong: ${e.message}")
                )
            }
        }
    }
}