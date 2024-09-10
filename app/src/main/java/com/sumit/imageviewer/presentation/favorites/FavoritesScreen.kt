package com.sumit.imageviewer.presentation.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun FavoritesScreen(
    snackBarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    favoriteImages: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    onBackClick: () -> Unit,
    onClickItem: (String) -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
    onSearchClick: () -> Unit,
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
                title = "Favorite Images",
                scrollBehavior = scrollBehavior,
                onSearchClick = onSearchClick,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )

            ImagesVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                list = favoriteImages,
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

        ZoomedImageCard(
            modifier = Modifier.padding(20.sdp),
            isVisible = showImagePreview,
            image = activeImage
        )

        if (favoriteImages.itemCount == 0) {
            EmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.sdp)
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.ic_img_empty_bookmark),
            contentDescription = "Empty Bookmark"
        )

        Spacer(Modifier.height(40.sdp))

        Text(
            text = "No Saved Images",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(Modifier.height(6.sdp))

        Text(
            text = "Your favorite Images will store here",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}