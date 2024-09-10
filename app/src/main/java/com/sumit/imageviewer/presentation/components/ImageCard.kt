package com.sumit.imageviewer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sumit.imageviewer.domain.model.UnsplashImage
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    image: UnsplashImage?,
    isFavorite: Boolean,
    onToggleFavoriteStatus: () -> Unit,
) {
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image?.imageUrlSmall)
        .crossfade(true)
        .build()

    val imageAspectRatio by remember {
        derivedStateOf {
            (image?.width?.toFloat() ?: 1f) / (image?.height?.toFloat() ?: 1f)
        }
    }

    Card(
        shape = RoundedCornerShape(10.sdp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(imageAspectRatio)
            .then(modifier)
    ) {
        Box {
            AsyncImage(
                model = imageRequest,
                contentDescription = "Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            FavoriteButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                isFavorite = isFavorite,
                onClick = onToggleFavoriteStatus
            )
        }
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    FilledIconToggleButton(
        modifier = modifier,
        checked = isFavorite,
        onCheckedChange = { onClick() },
        colors = IconButtonDefaults.filledIconToggleButtonColors(containerColor = Color.Transparent)
    ) {
        if (isFavorite)
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Filled Favorite Button"
            )
        else
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Bordered Favorite Button"
            )
    }
}