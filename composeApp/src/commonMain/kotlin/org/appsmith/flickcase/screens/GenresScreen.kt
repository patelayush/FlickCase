package org.appsmith.flickcase.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.appsmith.flickcase.components.GenreRow
import org.appsmith.flickcase.components.MovieCard
import org.appsmith.flickcase.viewmodel.HomeViewModel

@Composable
fun GenresScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var showSpinner by rememberSaveable { mutableStateOf(false) }
    if(!scrollState.canScrollForward && !homeViewModel.contentByMultipleGenre.isEmpty() && !homeViewModel.reachedEndOfMoviesByMultipleGenre.value){
       showSpinner = true
    }
    LaunchedEffect(homeViewModel.isLoading.value){
        if(!homeViewModel.isLoading.value){
            showSpinner = false
        }
    }
    LaunchedEffect(showSpinner){
        if(showSpinner){
            homeViewModel.loadMoreMoviesByMultipleGenres()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
        ) {
            GenreRow(
                modifier = Modifier.padding(top = 40.dp),
                genres = homeViewModel.contentGenres,
                selectedGenres = homeViewModel.selectedGenres,
                onGenreSelected = {
                    homeViewModel.resetContentByMultipleGenre()
                    homeViewModel.getMoviesByMultipleGenres()
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0, tween())
                    }
                },
                onGenreRemoved = {
                    if (homeViewModel.selectedGenres.isNotEmpty()) {
                        homeViewModel.resetContentByMultipleGenre()
                        homeViewModel.getMoviesByMultipleGenres()
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0, tween())
                        }
                    }
                }
            )
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 30.dp)
            ) {
                if (homeViewModel.contentByMultipleGenre.isEmpty() || homeViewModel.selectedGenres.isEmpty()) {
                    if (homeViewModel.selectedGenres.isEmpty()) {
                        Text(
                            text = "Search for ${if (homeViewModel.showMovies.value) "movies" else "tv shows"} by genre",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if(!homeViewModel.isLoading.value) {
                        Text(
                            text = "No results found.",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "If you're looking for ${if (homeViewModel.showMovies.value) "tv shows" else "movies"}, please toggle through Home screen.",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 5.dp).fillMaxWidth()
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                        ) {
                            homeViewModel.contentByMultipleGenre.forEach {
                                MovieCard(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .height(200.dp),
                                    movie = it,
                                    configuration = homeViewModel.configuration.value,
                                    showImageLoader = homeViewModel.isContentDetailsLoading.value,
                                    onCardClick = {
                                        homeViewModel.getContent(it)
                                    }
                                )
                            }
                        }
                        if(showSpinner) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.tertiary,
                                strokeWidth = 5.dp,
                                modifier = Modifier.padding(top = 20.dp).size(25.dp)
                            )
                        }
                        Spacer(Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}

