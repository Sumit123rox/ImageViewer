package com.sumit.imageviewer.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviwer.R
import ir.kaaveh.sdpcompose.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerAppBar(
    modifier: Modifier = Modifier,
    title: String = "Image Viewer",
    onSearchClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    navigationIcon: @Composable (() -> Unit) = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                    ) {
                        append(title.split(" ").first())
                    }

                    withStyle(
                        style = SpanStyle(color = MaterialTheme.colorScheme.secondary)
                    ) {
                        append(" ${title.split(" ").last()}")
                    }
                },
                fontWeight = FontWeight.ExtraBold
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = navigationIcon
    )
}

@Composable
fun FullScreenTopAppBar(
    modifier: Modifier = Modifier,
    image: UnsplashImage?,
    isVisible: Boolean,
    onBackClick: () -> Unit,
    onPhotographerNameClick: (String) -> Unit,
    onDownloadImageClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }

            AsyncImage(
                modifier = Modifier
                    .size(30.sdp)
                    .clip(CircleShape),
                model = image?.photographerProfileImgUrl,
                contentDescription = "Profile Image"
            )

            Spacer(Modifier.width(10.sdp))
            Column(
                modifier = Modifier.clickable {
                    image?.let {
                        onPhotographerNameClick(it.photographerProfileLink ?: "")
                    }
                }
            ) {
                Text(
                    text = image?.photographerName ?: "",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = image?.photographerUsername ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.weight(1f))
            IconButton(onClick = onDownloadImageClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = "Download Image Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
