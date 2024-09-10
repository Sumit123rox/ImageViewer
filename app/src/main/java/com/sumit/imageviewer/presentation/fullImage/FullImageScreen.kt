package com.sumit.imageviewer.presentation.fullImage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.presentation.components.DownloadOptionsBottomSheet
import com.sumit.imageviewer.presentation.components.FullScreenTopAppBar
import com.sumit.imageviewer.presentation.components.ImageDownloadOptions
import com.sumit.imageviewer.presentation.components.imageviwerLoadingBar
import com.sumit.imageviewer.utils.*
import com.sumit.imageviwer.R
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullImageScreen(
    snackBarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    image: UnsplashImage?,
    onBackClick: () -> Unit,
    onPhotographerNameClick: (String) -> Unit,
    onImageDownloadClick: (String, String?) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showBars by rememberSaveable { mutableStateOf(false) }
    val windowInsetsController = rememberWindowInsetsController()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isDownloadBottomSheetOpen by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        snackBarEvent.collectLatest { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        windowInsetsController.toggleStatusBar(show = showBars)
    }

    BackHandler(enabled = !showBars) {
        windowInsetsController.toggleStatusBar(show = true)
        onBackClick()
    }

    DownloadOptionsBottomSheet(
        isOpen = isDownloadBottomSheetOpen,
        sheetState = sheetState,
        onDismissRequest = { isDownloadBottomSheetOpen = false },
        onOptionClick = { option ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isDownloadBottomSheetOpen = false
            }

            val url = when (option) {
                ImageDownloadOptions.SMALL -> image?.imageUrlSmall
                ImageDownloadOptions.MEDIUM -> image?.imageUrlRegular
                ImageDownloadOptions.ORIGINAL -> image?.imageUrlRaw
            }

            url?.let {
                onImageDownloadClick(it, image?.description?.take(20))
                context.toast { "Downloading..." }
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            loge { "Width: ${this.maxWidth} Height: ${this.maxHeight}" }
            var scale by remember { mutableFloatStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val isImageZoomed by remember { derivedStateOf { scale != 1f } }
            val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale = max(scale * zoomChange, 1f)
                val maxX = (constraints.maxWidth * (scale - 1)) / 2
                val maxY = (constraints.maxHeight * (scale - 1)) / 2
                offset = Offset(
                    x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
                )
            }
            var isLoading by remember { mutableStateOf(true) }
            var isError by remember { mutableStateOf(false) }
            val imageLoader = rememberAsyncImagePainter(
                model = image?.imageUrlRaw,
                onState = { imageState ->
                    isLoading = imageState is AsyncImagePainter.State.Loading
                    isError = imageState is AsyncImagePainter.State.Error
                }
            )

            if (isLoading) {
                imageviwerLoadingBar()
            }

            Image(
                painter = if (isError.not()) imageLoader else painterResource(id =R.drawable.ic_error),
                contentDescription = "Full Screen Image",
                modifier = Modifier
                    .fillMaxSize()
                    .transformable(state = transformState)
                    .combinedClickable(
                        onDoubleClick = {
                            if (isImageZoomed) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scope.launch { transformState.animateZoomBy(zoomFactor = 3f) }
                            }
                        },
                        onClick = {
                            showBars = !showBars
                            windowInsetsController.toggleStatusBar(show = showBars)
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            )
        }

        FullScreenTopAppBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 5.sdp, vertical = 35.sdp),
            image = image,
            isVisible = showBars,
            onBackClick = onBackClick,
            onPhotographerNameClick = onPhotographerNameClick,
            onDownloadImageClick = { isDownloadBottomSheetOpen = true }
        )
    }
}