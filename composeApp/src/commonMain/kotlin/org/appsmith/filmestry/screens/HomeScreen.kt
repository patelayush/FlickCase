package org.appsmith.filmestry.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.app_icon
import filmestry.composeapp.generated.resources.ic_language
import org.appsmith.filmestry.APP_NAME
import org.appsmith.filmestry.components.MovieCard
import org.appsmith.filmestry.components.RegionPicker
import org.appsmith.filmestry.model.configuration.ConfigurationResponse
import org.appsmith.filmestry.model.movies.MoviesByGenre
import org.appsmith.filmestry.model.movies.MoviesResponse
import org.appsmith.filmestry.model.nowplayingmovies.NowPlayingMoviesResponse
import org.appsmith.filmestry.shared.getPlatform
import org.appsmith.filmestry.viewmodel.HomeViewModel
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
        if (homeViewModel.isLoading.value) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                strokeWidth = 5.dp,
                modifier = Modifier.align(Alignment.Center).size(50.dp)
            )
        } else {
            HomeScreenContent(
                modifier = Modifier.fillMaxSize(),
                trendingMovies = homeViewModel.trendingMovies.value,
                configuration = homeViewModel.configuration.value,
                nowPlayingMovies = homeViewModel.nowPlayingMovies.value,
                moviesByGenre = homeViewModel.moviesByGenre.value,
                showRegionPicker = {
                    showRegionSelector = true
                }
            )
        }

        if (!getPlatform().name.contains("web", true)) {
            RangePickerIcon(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 10.dp)
            ) {
                showRegionSelector = true
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
                homeViewModel.selectedRegion.value = it
                homeViewModel.refresh()
                showRegionSelector = false
            }
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    trendingMovies: MoviesResponse?,
    configuration: ConfigurationResponse?,
    nowPlayingMovies: NowPlayingMoviesResponse?,
    moviesByGenre: List<MoviesByGenre>,
    showRegionPicker: () -> Unit = {}
) {
    Column {
        if (getPlatform().name.contains("web", true)) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Image(
                        painter = painterResource(Res.drawable.app_icon),
                        contentDescription = "icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = APP_NAME,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                RangePickerIcon {
                    showRegionPicker()
                }
            }
        }
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Trending Movies",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 30.dp,
                        start = 20.dp
                    ),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            LazyRow(Modifier.padding(top = 10.dp)) {
                itemsIndexed(
                    trendingMovies?.results ?: listOf()
                ) { index, movie ->
                    MovieCard(
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = if (index == trendingMovies?.results?.lastIndex) 20.dp else 0.dp
                            )
                            .width(250.dp),
                        movie = movie,
                        configuration = configuration,
                        onCardClick = {

                        }
                    )
                }
            }

            Text(
                text = "In Theatres",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 30.dp,
                        start = 20.dp
                    ),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            LazyRow(Modifier.padding(top = 10.dp)) {
                itemsIndexed(
                    nowPlayingMovies?.results ?: listOf()
                ) { index, movie ->
                    MovieCard(
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = if (index == nowPlayingMovies?.results?.lastIndex) 20.dp else 0.dp
                            )
                            .width(250.dp),
                        movie = movie,
                        configuration = configuration,
                        onCardClick = {

                        }
                    )
                }
            }

            moviesByGenre.forEach {
                Text(
                    text = "Popular ${it.genre.name} Movies",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 30.dp,
                            start = 20.dp
                        ),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                LazyRow(Modifier.padding(top = 10.dp)) {
                    itemsIndexed(
                        it.movies ?: listOf()
                    ) { index, movie ->
                        MovieCard(
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    end = if (index == it.movies?.lastIndex) 20.dp else 0.dp
                                )
                                .width(250.dp),
                            movie = movie,
                            configuration = configuration,
                            onCardClick = {

                            }
                        )
                    }
                }
            }
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

