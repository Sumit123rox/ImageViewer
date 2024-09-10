package com.sumit.imageviewer.presentation.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.domain.repository.ImageRepository
import com.sumit.imageviewer.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    private var images by mutableStateOf<List<UnsplashImage>>(emptyList())

    val favoriteImage: StateFlow<PagingData<UnsplashImage>> = imageRepository.getAllFavoriteImages()
        .catch { exception ->
            _snackBarEvent.send(SnackBarEvent(message = "Something went wrong: ${exception.message}"))
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = PagingData.empty()
        )

    val favoriteImageIds: StateFlow<List<String>> = imageRepository.getFavoriteImageIds()
        .catch { exception ->
            _snackBarEvent.send(SnackBarEvent(message = "Something went wrong: ${exception.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    fun toggleFavoriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                imageRepository.toggleFavoriteStatus(image)
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.send(SnackBarEvent(message = "Something went wrong: ${e.message}"))
            }
        }
    }
}