package com.sumit.imageviewer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.paging.compose.LazyPagingItems
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.utils.loge
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ImagesVerticalGrid(
    modifier: Modifier = Modifier,
    list: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    onClickItem: (String) -> Unit,
    onImageDragStart: (UnsplashImage?) -> Unit,
    onImageDragEnd: () -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(100.sdp),
        contentPadding = PaddingValues(8.sdp),
        modifier = modifier,
        verticalItemSpacing = 8.sdp,
        horizontalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        items(count = list.itemCount) { index ->
            val image = list[index]
            loge { "Id: ${image?.id}" }
            ImageCard(
                image = image,
                modifier = Modifier
                    .clickable {
                        loge { "Item Clicked $image" }
                        image?.id?.let { onClickItem(it) }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { onImageDragStart(image) },
                            onDragCancel = onImageDragEnd,
                            onDragEnd = onImageDragEnd,
                            onDrag = { _, _ ->

                            }
                        )
                    },
                isFavorite = favoriteImageIds.contains(image?.id),
                onToggleFavoriteStatus = {
                    image?.let {
                        onToggleFavoriteStatus(it)
                    }
                }
            )
        }
    }
}