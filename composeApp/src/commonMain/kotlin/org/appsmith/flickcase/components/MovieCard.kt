package org.appsmith.flickcase.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.movies.Movie
import kotlin.math.roundToInt

@Composable
fun MovieCard(
    modifier: Modifier,
    movie: Movie? = null,
    showImageLoader: Boolean = false,
    configuration: ConfigurationResponse?,
    onCardClick: (Int?) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var showDetailsLoading by remember { mutableStateOf(false) }
    if(showDetailsLoading && !showImageLoader){
        showDetailsLoading = false
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isLoading) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
        ),
        onClick = {
            onCardClick(movie?.id)
            showDetailsLoading = true
        },
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        content = {
            Column {
                FlickCaseImageLoader(
                    modifier = Modifier.fillMaxSize(),
                    baseUrl = configuration?.images?.secure_base_url ?: "",
                    path = movie?.poster_path?.takeIf { it.isNotBlank() } ?: movie?.backdrop_path ?: "",
                    voteAverage = ((movie?.vote_average ?: 0.0) * 10).roundToInt(),
                    additionalLoader = showDetailsLoading
                )
            }
        }
    )
}