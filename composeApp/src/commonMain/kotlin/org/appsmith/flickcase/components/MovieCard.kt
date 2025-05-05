package org.appsmith.flickcase.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import coil3.compose.AsyncImage
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.ic_star_filled
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.movies.Movie
import org.appsmith.flickcase.shared.round
import org.jetbrains.compose.resources.painterResource

@Composable
fun MovieCard(
    modifier: Modifier,
    movie: Movie? = null,
    showImageLoader: Boolean = false,
    configuration: ConfigurationResponse?,
    onCardClick: (Int?) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if(isLoading) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
        ),
        onClick = {
            onCardClick(movie?.id)
        },
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        content = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "${configuration?.images?.secure_base_url}original${movie?.poster_path?.takeIf { it.isNotBlank() } ?: movie?.backdrop_path}",
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        filterQuality = FilterQuality.High,
                        onLoading = {
                            isLoading = true
                        },
                        onSuccess = {
                            isLoading = false
                        },
                        onError = {
                            isLoading = false
                            println(it.result.throwable.message)
                        },
                        modifier = Modifier.fillMaxSize()
                            .clip(shape = RoundedCornerShape(15.dp)),
                    )
                    if (isLoading || showImageLoader) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            strokeWidth = 5.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .wrapContentWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = 5.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = movie?.vote_average?.round(1)?.toString() ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            painter = painterResource(Res.drawable.ic_star_filled),
                            contentDescription = "star_icon",
                            modifier = Modifier.offset(2.dp)
                                .size(MaterialTheme.typography.bodySmall.fontSize.value.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    )
}