package com.sumit.imageviewer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.domain.repository.ImageRepository
import com.sumit.imageviewer.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    val images: StateFlow<PagingData<UnsplashImage>> = imageRepository.getEditorialFeedImages()
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