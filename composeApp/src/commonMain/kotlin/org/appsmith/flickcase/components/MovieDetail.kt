package org.appsmith.flickcase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.appsmith.flickcase.model.cast.CastDetailsResponse
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailSheet(
    configuration: ConfigurationResponse?,
    movie: MovieDetailsResponse?,
    cast: CastDetailsResponse?,
    onDismissRequest: () -> Unit = {}
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(true) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    LaunchedEffect(bottomSheetState) {
        if (bottomSheetState.hasExpandedState) {
            skipPartiallyExpanded = true
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .widthIn(max = 600.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onDismissRequest = {
                openBottomSheet = false
                onDismissRequest()
            },
            sheetState = bottomSheetState,
        ) {
            Box(Modifier.padding(bottom = 56.dp)) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                ) {
                    FlickCaseImageLoader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        baseUrl = configuration?.images?.secure_base_url ?: "",
                        path = movie?.backdrop_path?.takeIf { it.isNotBlank() }
                            ?: movie?.poster_path ?: "",
                    )
                    Column(Modifier.padding(bottom = 30.dp)) {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = movie?.title ?: movie?.name ?: "",
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
                            modifier = Modifier.padding(top = 5.dp),
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
                                    text = "${if (movie?.first_air_date != null) "Released" else movie?.status ?: ""} in: ",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = (movie?.release_date?.takeIf { it.isNotBlank() }
                                        ?: movie?.first_air_date)?.takeIf { it.isNotBlank() }
                                        ?.substring(startIndex = 0, endIndex = 4) ?: "",
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
                    }
                }
            }
        }
    }
}