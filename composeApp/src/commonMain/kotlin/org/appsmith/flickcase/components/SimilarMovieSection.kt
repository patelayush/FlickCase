package org.appsmith.flickcase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.movies.Movie

@Composable
fun SimilarMoviesSection(modifier: Modifier, movies: List<Movie?>, configuration: ConfigurationResponse?, loadMoreSimilarMovies:() -> Unit, onMovieClicked:(Int?) -> Unit) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            // Get the total number of items in the list
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            // Get the index of the last visible item
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Check if we have scrolled near the end of the list and more items should be loaded
            lastVisibleItemIndex >= (totalItemsCount - 2)
        }
    }

    // Launch a coroutine to load more items when shouldLoadMore becomes true
    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }  // Ensure that we load more items only when needed
            .collect {
                loadMoreSimilarMovies()
            }
    }

    Text(
        text = "More like this",
        modifier = modifier.padding(horizontal = 15.dp),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.tertiary,
        style = MaterialTheme.typography.titleMedium,
    )

    LazyRow(
        state = listState,
        modifier = Modifier.padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(
            movies
        ) { index, movie ->
            MovieCard(
                modifier = Modifier
                    .padding(
                        start = if(index == 0) 15.dp else 0.dp,
                        end = if(index == movies.lastIndex) 15.dp else 0.dp
                    )
                    .width(160.dp)
                    .height(200.dp),
                movie = movie,
                configuration = configuration,
                onCardClick = {
                    onMovieClicked(it)
                }
            )
        }
    }
}
