package org.appsmith.flickcase.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.app_icon
import filmestry.composeapp.generated.resources.ic_language
import org.appsmith.flickcase.APP_NAME
import org.appsmith.flickcase.components.MovieCard
import org.appsmith.flickcase.components.RegionPicker
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.movies.Movie
import org.appsmith.flickcase.model.movies.MoviesByGenre
import org.appsmith.flickcase.model.movies.MoviesResponse
import org.appsmith.flickcase.model.nowplayingmovies.NowPlayingMoviesResponse
import org.appsmith.flickcase.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    var showRegionSelector by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (homeViewModel.trendingMovies.value == null) {
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
                    trendingMovies = homeViewModel.trendingMovies.value,
                    configuration = homeViewModel.configuration.value,
                    nowPlayingMovies = homeViewModel.nowPlayingMovies.value,
                    moviesByGenre = homeViewModel.moviesByGenre.value,
                    showMovieLoader = homeViewModel.isMovieDetailsLoading.value,
                    onMovieClicked = {
                        homeViewModel.getMovieDetails(it)
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
    trendingMovies: MoviesResponse?,
    configuration: ConfigurationResponse?,
    nowPlayingMovies: NowPlayingMoviesResponse?,
    moviesByGenre: List<MoviesByGenre>,
    showMovieLoader: Boolean,
    onMovieClicked: (Int?) -> Unit
) {

    val categories: List<Pair<String, List<Movie?>>> = remember {
        mutableListOf(
            Pair("Trending Movies", trendingMovies?.results ?: listOf()),
            Pair("In Theatres", nowPlayingMovies?.results ?: listOf()),
        ).apply {
            moviesByGenre.forEach {
                add(Pair(("Popular ${it.genre.name}"), it.movies ?: listOf()))
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
                        top = if (index == 0) 20.dp else 0.dp,
                        start = 20.dp
                    ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            LazyRow(
                Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    categories.second
                ) { index, movie ->
                    MovieCard(
                        modifier = Modifier
                            .padding(
                                start = if (index == 0) 20.dp else 0.dp,
                                end = if (index == trendingMovies?.results?.lastIndex) 20.dp else 0.dp
                            )
                            .width(160.dp),
                        movie = movie,
                        showImageLoader = showMovieLoader,
                        configuration = configuration,
                        onCardClick = {
                            onMovieClicked(it)
                        }
                    )
                }
            }

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

