package org.appsmith.flickcase.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse

@Composable
fun MovieDetails(
    showDialog: Boolean,
    configuration: ConfigurationResponse?,
    movie: MovieDetailsResponse?,
    onDismiss: () -> Unit,
) {

    if (showDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = onDismiss,
            content = {
                Column(
                    modifier = Modifier
                        .padding(vertical = 80.dp, horizontal = 20.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    var isImageLoading by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "${configuration?.images?.secure_base_url}original${movie?.backdrop_path}",
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            filterQuality = FilterQuality.High,
                            onLoading = {
                                isImageLoading = true
                            },
                            onSuccess = {
                                isImageLoading = false
                            },
                            onError = {
                                isImageLoading = false
                                println(it.result.throwable.message)
                            },
                            modifier = Modifier.fillMaxSize()
                                .clip(shape = RoundedCornerShape(10.dp)),
                        )
                        if (isImageLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onTertiary,
                                strokeWidth = 5.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column(Modifier.padding(horizontal = 15.dp).padding(bottom = 30.dp)) {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = movie?.title ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            modifier = Modifier.padding(),
                            text = movie?.tagline ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        GenreSection(
                            modifier = Modifier.padding(top = 5.dp),
                            genres = movie?.genres ?: listOf()
                        )
                        Row(
                            modifier = Modifier.padding(top = 15.dp),
                        ) {
                            Text(
                                text ="${movie?.status ?: ""} in: ",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = movie?.release_date?.substring(startIndex = 0, endIndex = 4) ?: "",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = movie?.overview ?: "",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        )
    }
}