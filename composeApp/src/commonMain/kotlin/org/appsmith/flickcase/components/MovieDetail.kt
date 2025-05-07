package org.appsmith.flickcase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.appsmith.flickcase.model.cast.CastDetailsResponse
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse
import org.appsmith.flickcase.model.movies.Movie
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailSheet(
    configuration: ConfigurationResponse?,
    movie: MovieDetailsResponse?,
    similarMovies: List<Movie?>?,
    showDetailsLoader: Boolean,
    cast: CastDetailsResponse?,
    loadMoreSimilarMovies: () -> Unit,
    onMovieClicked: (Int?) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    var movieDetailsLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(movie) {
        movieDetailsLoading = false
    }

    LaunchedEffect(bottomSheetState) {
        if (bottomSheetState.hasExpandedState) {
            skipPartiallyExpanded = true
        }
    }

    ModalBottomSheet(
        modifier = Modifier
            .statusBarsPadding()
            .widthIn(max = 600.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = bottomSheetState,
        scrimColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        tonalElevation = 0.dp,
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (movieDetailsLoading || showDetailsLoader) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 5.dp,
                    modifier = Modifier.size(50.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    FlickCaseImageLoader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        baseUrl = configuration?.images?.secure_base_url ?: "",
                        path = movie?.backdrop_path?.takeIf { it.isNotBlank() }
                            ?: movie?.poster_path ?: "",
                        shape = RectangleShape
                    )
                    Column(Modifier.padding(horizontal = 15.dp).padding(bottom = 30.dp)) {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "${movie?.title ?: movie?.name ?: ""} (${
                                (movie?.release_date?.takeIf { it.isNotBlank() }
                                    ?: movie?.first_air_date)?.takeIf { it.isNotBlank() }
                                    ?.substring(startIndex = 0, endIndex = 4) ?: ""
                            })",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (!movie?.tagline.isNullOrBlank()) {
                            Text(
                                modifier = Modifier.padding(),
                                text = movie?.tagline ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        GenreSection(
                            modifier = Modifier.padding(top = 10.dp),
                            genres = movie?.genres ?: listOf()
                        )
                        Row(
                            modifier = Modifier.padding(top = 15.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    text = "Status: ",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = movie?.status ?: "",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Row {
                                Text(
                                    text = "User score: ",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${((movie?.vote_average ?: 0.0) * 10).roundToInt()}%",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        if (!movie?.overview.isNullOrBlank()) {
                            ExpandableText(
                                modifier = Modifier.padding(top = 15.dp),
                                text = movie?.overview ?: "",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        if (!cast?.cast.isNullOrEmpty()) {
                            CastSection(
                                modifier = Modifier.padding(top = 20.dp),
                                cast = cast?.cast,
                                baseUrl = configuration?.images?.secure_base_url
                            )
                        }
                        if (movie?.last_episode_to_air != null) {
                            LastEpisodeToAirSection(
                                modifier = Modifier.padding(top = 20.dp),
                                tvShow = movie,
                                baseUrl = configuration?.images?.secure_base_url,
                                seasonOverview = movie.seasons?.lastOrNull()?.overview
                            )
                        }
                        if (!similarMovies.isNullOrEmpty()) {
                            SimilarMoviesSection(
                                modifier = Modifier.padding(top = 20.dp),
                                movies = similarMovies,
                                configuration = configuration,
                                loadMoreSimilarMovies = loadMoreSimilarMovies,
                                onMovieClicked = {
                                    movieDetailsLoading = true
                                    onMovieClicked(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}