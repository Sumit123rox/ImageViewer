package com.sumit.imageviewer.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.skydoves.cloudy.Cloudy
import com.sumit.imageviewer.domain.model.UnsplashImage
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ZoomedImageCard(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    image: UnsplashImage?
) {

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image?.imageUrlRegular)
        .crossfade(true)
        .placeholderMemoryCacheKey(MemoryCache.Key(image?.imageUrlSmall ?: ""))
        .build()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible)
            Cloudy(modifier = Modifier.fillMaxSize(), radius = 16) { }
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Card(
                modifier = modifier
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(10.sdp)
                            .clip(CircleShape)
                            .size(25.sdp),
                        model = image?.photographerProfileImgUrl,
                        contentDescription = "Photographer Image"
                    )
                    Text(
                        text = image?.photographerName ?: "Anonymous",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = imageRequest,
                    contentDescription = "Big Image"
                )
            }
        }
    }
}