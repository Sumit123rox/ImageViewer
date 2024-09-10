package com.sumit.imageviewer.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.paging.compose.LazyPagingItems
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.presentation.components.ImageViewerAppBar
import com.sumit.imageviewer.presentation.components.ImagesVerticalGrid
import com.sumit.imageviewer.presentation.components.ZoomedImageCard
import com.sumit.imageviewer.utils.SnackBarEvent
import com.sumit.imageviwer.R
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    snackBarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    images: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    onClickItem: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFABClick: () -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {

    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageViewerAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick
            )
            ImagesVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                list = images,
                onClickItem = onClickItem,
                favoriteImageIds = favoriteImageIds,
                onImageDragStart = {
                    activeImage = it
                    showImagePreview = true
                },
                onImageDragEnd = {
                    showImagePreview = false
                },
                onToggleFavoriteStatus = onToggleFavoriteStatus
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.sdp),
            onClick = onFABClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_save),
                contentDescription = "Favorites"
            )
        }

        ZoomedImageCard(
            modifier = Modifier.padding(20.sdp),
            isVisible = showImagePreview,
            image = activeImage
        )
    }
}