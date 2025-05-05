package org.appsmith.flickcase.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.app_icon
import filmestry.composeapp.generated.resources.ic_language
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.appsmith.flickcase.APP_NAME
import org.appsmith.flickcase.components.MovieCard
import org.appsmith.flickcase.components.RegionPicker
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.movies.Movie
import org.appsmith.flickcase.model.movies.MoviesByGenre
import org.appsmith.flickcase.model.movies.MoviesResponse
import org.appsmith.flickcase.model.nowplayingmovies.NowPlayingMoviesResponse
import org.appsmith.flickcase.nowPlayingCategory
import org.appsmith.flickcase.trendingCategory
import org.appsmith.flickcase.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    var showRegionSelector by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (homeViewModel.trendingContent.isEmpty()) {
            homeViewModel.isLoading.value = true
            homeViewModel.init()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.app_icon),
                        contentDescription = "icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = APP_NAME,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.1.em
                    )
                }
                RangePickerIcon {
                    showRegionSelector = true
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .then(
                            if(homeViewModel.showMovies.value){
                                Modifier.background(
                                    color = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            } else
                                Modifier
                                    .background(Color.Transparent)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.tertiaryContainer,
                                        RoundedCornerShape(20.dp)
                                    )

                        ).padding(horizontal = 15.dp, vertical = 5.dp)
                        .clickable {
                            if(!homeViewModel.showMovies.value) {
                                homeViewModel.showMovies.value = true
                                homeViewModel.refresh()
                            }
                        }
                ) {
                    Text(
                        text = "Movies",
                        style = MaterialTheme.typography.bodySmall,
                        color =  if(homeViewModel.showMovies.value) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.1.em
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .then(
                            if(!homeViewModel.showMovies.value){
                                Modifier.background(
                                    color = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            } else
                                Modifier
                                    .background(Color.Transparent)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.tertiaryContainer,
                                        RoundedCornerShape(20.dp)
                                    )

                        )
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .clickable {
                            if(homeViewModel.showMovies.value) {
                                homeViewModel.showMovies.value = false
                                homeViewModel.refresh()
                            }
                        }
                ) {
                    Text(
                        text = "Tv Shows",
                        style = MaterialTheme.typography.bodySmall,
                        color =  if(!homeViewModel.showMovies.value) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.1.em
                    )
                }
            }
            if (homeViewModel.isLoading.value) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary,
                        strokeWidth = 5.dp,
                        modifier = Modifier.size(50.dp)
                    )
                }
            } else {
                HomeScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    contentTypeMovie = homeViewModel.showMovies.value,
                    trendingMovies = homeViewModel.trendingContent,
                    configuration = homeViewModel.configuration.value,
                    nowPlayingMovies = homeViewModel.nowPlayingMovies,
                    moviesByGenre = homeViewModel.contentByGenre,
                    showMovieLoader = homeViewModel.isContentDetailsLoading.value,
                    isLoadingAdditionalMovies = homeViewModel.isLoadingAdditionalMovies.value,
                    onMovieClicked = {
                        homeViewModel.getContentDetails(it)
                    },
                    loadMoreMovies = {
                        homeViewModel.loadMoreMovies(it)
                    }
                )
            }
        }
    }

    RegionPicker(
        showDialog = showRegionSelector,
        regions = homeViewModel.countries.value ?: listOf(),
        selectedRegion = homeViewModel.selectedRegion.value,
        onDismiss = {
            showRegionSelector = false
        },
        onRegionSelected = {
            homeViewModel.setSelectedRegion(it)
            showRegionSelector = false
        }
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    contentTypeMovie: Boolean = false,
    trendingMovies: List<Movie?>?,
    configuration: ConfigurationResponse?,
    nowPlayingMovies: List<Movie?>?,
    moviesByGenre: List<MoviesByGenre>,
    showMovieLoader: Boolean,
    isLoadingAdditionalMovies: Boolean,
    onMovieClicked: (Int?) -> Unit,
    loadMoreMovies: (String) -> Unit
) {

    val categories: List<Pair<String, List<Movie?>>> = remember (contentTypeMovie) {
        mutableListOf(
            Pair("$trendingCategory ${if(contentTypeMovie) "Movies" else "Tv Shows"}", trendingMovies ?: listOf()),
        ).apply {
            if(contentTypeMovie){
               add(Pair(nowPlayingCategory, nowPlayingMovies ?: listOf()))
            }
            moviesByGenre.forEach {
                add(Pair("Popular ${it.genre.name}", it.movies ?: listOf()))
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(categories.filter { it.second.isNotEmpty() }) { index, categories ->
            Text(
                text = categories.first,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp,
                        start = 20.dp
                    ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            val listState = rememberLazyListState()

            val shouldLoadMore = remember {
                derivedStateOf {
                    // Get the total number of items in the list
                    val totalItemsCount = listState.layoutInfo.totalItemsCount
                    // Get the index of the last visible item
                    val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    // Check if we have scrolled near the end of the list and more items should be loaded
                    lastVisibleItemIndex >= (totalItemsCount - 1) && !isLoadingAdditionalMovies
                }
            }

            // Launch a coroutine to load more items when shouldLoadMore becomes true
            LaunchedEffect(listState) {
                snapshotFlow { shouldLoadMore.value }
                    .distinctUntilChanged()
                    .filter { it }  // Ensure that we load more items only when needed
                    .collect {
                        loadMoreMovies(categories.first)
                    }
            }

            LazyRow(
                state = listState,
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    categories.second
                ) { index, movie ->
                    MovieCard(
                        modifier = Modifier
                            .padding(
                                start = if (index == 0) 20.dp else 0.dp,
                                end = if (index == categories.second.lastIndex) 20.dp else 0.dp
                            )
                            .width(160.dp)
                            .height(200.dp),
                        movie = movie,
                        showImageLoader = showMovieLoader,
                        configuration = configuration,
                        onCardClick = {
                            onMovieClicked(it)
                        }
                    )
                }
                if (isLoadingAdditionalMovies) {
                    item {
                        MovieCard(
                            modifier = Modifier
                                .padding(
                                    start = if (index == 0) 20.dp else 0.dp,
                                    end = if (index == categories.second.lastIndex) 0.dp else 0.dp
                                )
                                .width(160.dp)
                                .height(200.dp),
                            movie = null,
                            showImageLoader = isLoadingAdditionalMovies,
                            configuration = configuration,
                            onCardClick = {
                                onMovieClicked(it)
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun RangePickerIcon(modifier: Modifier = Modifier, showRegionPicker: () -> Unit) {
    Icon(
        painter = painterResource(Res.drawable.ic_language),
        contentDescription = "language_selector",
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = modifier
            .size(24.dp)
            .clickable {
                showRegionPicker()
            },
    )
}

