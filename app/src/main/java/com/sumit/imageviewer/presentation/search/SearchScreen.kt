package com.sumit.imageviewer.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.paging.compose.LazyPagingItems
import com.sumit.imageviewer.domain.model.UnsplashImage
import com.sumit.imageviewer.presentation.components.ImagesVerticalGrid
import com.sumit.imageviewer.presentation.components.ZoomedImageCard
import com.sumit.imageviewer.utils.SnackBarEvent
import com.sumit.imageviewer.utils.searchKeywords
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    snackBarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    searchedImages: LazyPagingItems<UnsplashImage>,
    searchQuery: String,
    favoriteImageIds: List<String>,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClickItem: (String) -> Unit,
    onSearch: (String) -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var isSuggestionChipsVisible by remember { mutableStateOf(false) }

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

    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SearchBar(
                modifier = Modifier
                    .padding(vertical = 8.sdp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isSuggestionChipsVisible = it.isFocused },
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) },
                onSearch = {
                    onSearch(searchQuery)
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                },
                active = false,
                placeholder = { Text(text = "Search...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) onSearchQueryChange("") else onBackClick()
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear Search")
                    }
                },
                onActiveChange = {},
                content = {}
            )

            AnimatedVisibility(visible = isSuggestionChipsVisible) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.sdp),
                    horizontalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    items(searchKeywords) { keyword ->
                        SuggestionChip(
                            onClick = {
                                onSearchQueryChange(keyword)
                                onSearch(keyword)
                                keyboardController?.hide()
                                focusManager.clearFocus(force = true)
                            },
                            label = {
                                Text(text = keyword)
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            ImagesVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                list = searchedImages,
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
    }
}