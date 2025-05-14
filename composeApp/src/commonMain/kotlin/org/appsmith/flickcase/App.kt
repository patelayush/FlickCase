package org.appsmith.flickcase

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.setSingletonImageLoaderFactory
import kotlinx.coroutines.launch
import org.appsmith.flickcase.components.BottomNavigationBar
import org.appsmith.flickcase.components.MovieDetailSheet
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.screens.AboutScreen
import org.appsmith.flickcase.screens.GenresScreen
import org.appsmith.flickcase.screens.HomeScreen
import org.appsmith.flickcase.screens.SearchScreen
import org.appsmith.flickcase.screens.WelcomeScreen
import org.appsmith.flickcase.shared.getAsyncImageLoader
import org.appsmith.flickcase.theme.AlwaysDarkTheme
import org.appsmith.flickcase.viewmodel.HomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    client: MovieApiClient,
    homeViewModel: HomeViewModel = viewModel { HomeViewModel(client) },
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    AlwaysDarkTheme {
        // to enable caching through coil. This will set context for the imageLoader factory
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        Scaffold(
            modifier = Modifier
                .blur(
                    if (homeViewModel.openMovieDetailSheet.value || homeViewModel.showRegionSelector.value) 4.dp else 0.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .animateContentSize(tween())
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = ScaffoldDefaults.contentWindowInsets.asPaddingValues()
                        .calculateTopPadding(),
                ),
            backgroundColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (homeViewModel.currentScreen.value != Screen.Welcome) {
                    BottomNavigationBar(
                        currentRoute = homeViewModel.currentScreen.value,
                        onItemClick = {
                            homeViewModel.currentScreen.value = it
                        }
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 30.dp)
                        .fillMaxWidth(),
                    snackbar = {
                        Snackbar(
                            shape = RoundedCornerShape(10.dp),
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            content = {
                                Text(
                                    text = it.visuals.message,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                        )
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(LayoutDirection.Ltr),
                    end = it.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = it.calculateBottomPadding() - 10.dp
                ),
                contentAlignment = Alignment.Center
            ) {
                when (homeViewModel.currentScreen.value) {
                    Screen.Welcome -> {
                        WelcomeScreen(
                            homeViewModel = homeViewModel
                        )
                    }

                    Screen.Home -> {
                        HomeScreen(
                            homeViewModel = homeViewModel,
                        )
                    }

                    Screen.Search -> {
                        SearchScreen(
                            homeViewModel = homeViewModel
                        )
                    }

                    Screen.Genres -> {
                        GenresScreen(
                            homeViewModel = homeViewModel
                        )
                    }

                    Screen.About -> {
                        AboutScreen()
                    }
                }
            }

            if (homeViewModel.openMovieDetailSheet.value) {
                MovieDetailSheet(
                    movie = homeViewModel.contentDetails.value,
                    cast = homeViewModel.castDetails.value,
                    similarMovies = homeViewModel.similarContent.toList(),
                    configuration = homeViewModel.configuration.value,
                    showDetailsLoader = homeViewModel.isContentDetailsLoading.value,
                    onDismissRequest = {
                        homeViewModel.openMovieDetailSheet.value = false
                        homeViewModel.contentDetails.value = null
                    },
                    loadMoreSimilarMovies = {
                        homeViewModel.loadMoreSimilarMovies()
                    },
                    onMovieClicked = {
                        homeViewModel.getContent(it)
                    })
            }
            if (homeViewModel.errorMessage.value.isNotBlank()) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = homeViewModel.errorMessage.value,
                        duration = SnackbarDuration.Short,
                    )
                    homeViewModel.errorMessage.value = ""
                }
            }
        }
    }
}