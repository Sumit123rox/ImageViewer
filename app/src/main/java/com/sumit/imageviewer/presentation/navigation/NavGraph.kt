package com.sumit.imageviewer.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.sumit.imageviewer.presentation.favorites.FavoritesScreen
import com.sumit.imageviewer.presentation.favorites.FavoritesViewModel
import com.sumit.imageviewer.presentation.fullImage.FullImageScreen
import com.sumit.imageviewer.presentation.fullImage.FullImageViewModel
import com.sumit.imageviewer.presentation.home.HomeScreen
import com.sumit.imageviewer.presentation.home.HomeViewModel
import com.sumit.imageviewer.presentation.navigation.Routes.*
import com.sumit.imageviewer.presentation.profile.ProfileScreen
import com.sumit.imageviewer.presentation.search.SearchScreen
import com.sumit.imageviewer.presentation.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarHostState: SnackbarHostState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {
        composable<HomeScreen> {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val images = homeViewModel.images.collectAsLazyPagingItems()
            val favoriteImageIds by homeViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            HomeScreen(
                snackBarHostState = snackbarHostState,
                snackBarEvent = homeViewModel.snackBarEvent,
                scrollBehavior = scrollBehavior,
                images = images,
                favoriteImageIds = favoriteImageIds,
                onClickItem = {
                    navController.navigate(FullImageScreen(it))
                },
                onSearchClick = {
                    navController.navigate(SearchScreen)
                },
                onFABClick = {
                    navController.navigate(FavoritesScreen)
                },
                onToggleFavoriteStatus = { homeViewModel.toggleFavoriteStatus(it) }
            )
        }

        composable<SearchScreen> {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            val searchedImages = searchViewModel.searchImages.collectAsLazyPagingItems()
            val favoriteImageIds by searchViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            SearchScreen(
                snackBarHostState = snackbarHostState,
                snackBarEvent = searchViewModel.snackBarEvent,
                searchedImages = searchedImages,
                favoriteImageIds = favoriteImageIds,
                onBackClick = {
                    navController.navigateUp()
                },
                onClickItem = { navController.navigate(FullImageScreen(it)) },
                onSearch = { searchViewModel.searchImages(it) },
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onToggleFavoriteStatus = { searchViewModel.toggleFavoriteStatus(it) }
            )
        }

        composable<FavoritesScreen> {
            val favoritesViewModel = hiltViewModel<FavoritesViewModel>()
            val favoriteImageIds by favoritesViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            FavoritesScreen(
                snackBarHostState = snackbarHostState,
                snackBarEvent = favoritesViewModel.snackBarEvent,
                favoriteImages = favoritesViewModel.favoriteImage.collectAsLazyPagingItems(),
                favoriteImageIds = favoriteImageIds,
                onBackClick = { navController.navigateUp() },
                onClickItem = { navController.navigate(FullImageScreen(it)) },
                onToggleFavoriteStatus = { favoritesViewModel.toggleFavoriteStatus(it) },
                onSearchClick = { navController.navigate(SearchScreen) },
                scrollBehavior = scrollBehavior,
            )
        }

        composable<FullImageScreen> {
            val fullScreeViewModel = hiltViewModel<FullImageViewModel>()
            FullImageScreen(
                snackBarHostState = snackbarHostState,
                snackBarEvent = fullScreeViewModel.snackBarEvent,
                image = fullScreeViewModel.image,
                onBackClick = {
                    navController.navigateUp()
                },
                onPhotographerNameClick = {
                    navController.navigate(ProfileScreen(it))
                },
                onImageDownloadClick = { url, fileName ->
                    fullScreeViewModel.downloadImage(url, fileName)
                }
            )
        }

        composable<ProfileScreen> { navBackStackEntery ->
            val profileLink = navBackStackEntery.toRoute<ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}