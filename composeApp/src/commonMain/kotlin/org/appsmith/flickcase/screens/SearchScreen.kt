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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.appsmith.flickcase.components.FilmestrySearchBar
import org.appsmith.flickcase.components.MovieCard
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.viewmodel.HomeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    client: MovieApiClient,
    homeViewModel: HomeViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var onTyping by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilmestrySearchBar(
                query = searchQuery,
                onQueryChange = { newQuery ->
                    searchQuery = newQuery
                    onTyping = true
                },
                onSearch = { query ->
                    homeViewModel.searchMovie(query)
                    onTyping = false
                }
            )
            if (homeViewModel.searchedMovies.value?.isNotEmpty() == true && searchQuery.isNotBlank() && !onTyping) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                append("Results for ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiary,
                                )
                            ) {
                                append(searchQuery)
                            }
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                    FlowRow(
                        modifier = Modifier.padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        homeViewModel.searchedMovies.value?.forEach {
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
            } else if (homeViewModel.searchedMovies.value?.isEmpty() == true && searchQuery.isNotBlank()) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 20.dp)
                ) {
                    if (!onTyping) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    append("No results found for ")
                                }
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.tertiary,
                                    )
                                ) {
                                    append(searchQuery)
                                }
                            },
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
        if (homeViewModel.isLoading.value) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 5.dp,
                    modifier = Modifier.align(Alignment.Center).size(50.dp)
                )
            }
        }
    }
}

