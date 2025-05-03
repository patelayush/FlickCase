package org.appsmith.flickcase.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.appsmith.flickcase.components.GenreRow
import org.appsmith.flickcase.components.MovieCard
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.viewmodel.HomeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenresScreen(
    modifier: Modifier = Modifier,
    client: MovieApiClient,
    homeViewModel: HomeViewModel
) {
    var showResults by remember { mutableStateOf(false) }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
        ) {
            GenreRow(
                modifier = Modifier.padding(top = 40.dp),
                genres = homeViewModel.movieGenres.value ?: listOf(),
                onGenreSelected = {
                    showResults = true
                    homeViewModel.selectedMovieGenres.value.add(it)
                    homeViewModel.getMoviesByMultipleGenres()
                },
                onGenreRemoved = {
                    homeViewModel.selectedMovieGenres.value.remove(it)
                    if (homeViewModel.selectedMovieGenres.value.isNotEmpty()) {
                        homeViewModel.getMoviesByMultipleGenres()
                    } else {
                        showResults = false
                    }
                }
            )
            Column(Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState())) {
                if (!showResults) {
                    Text(
                        text = "Search for movies by genre",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        homeViewModel.moviesByMultipleGenre.value.forEach {
                            MovieCard(
                                modifier = Modifier
                                    .width(180.dp),
                                movie = it,
                                configuration = homeViewModel.configuration.value,
                                showImageLoader = homeViewModel.isMovieDetailsLoading.value,
                                onCardClick = {
                                    homeViewModel.getMovieDetails(it)
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                }
            }
        }
    }
}

