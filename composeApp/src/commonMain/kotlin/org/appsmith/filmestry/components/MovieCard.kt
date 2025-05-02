package org.appsmith.filmestry.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.ic_star_filled
import org.appsmith.filmestry.model.configuration.ConfigurationResponse
import org.appsmith.filmestry.model.movies.Movie
import org.appsmith.filmestry.shared.round
import org.jetbrains.compose.resources.painterResource

@Composable
fun MovieCard(
    modifier: Modifier,
    movie: Movie?,
    configuration: ConfigurationResponse?,
    onCardClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        onClick = onCardClick,
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        content = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = configuration?.images?.base_url + "original" + movie?.backdrop_path,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
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
                            .clip(shape = RoundedCornerShape(15.dp))
                    )
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onTertiary,
                            strokeWidth = 5.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Row(
                    Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie?.title ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = movie?.vote_average?.round(1)?.toString() ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            painter = painterResource(Res.drawable.ic_star_filled),
                            contentDescription = "star_icon",
                            modifier = Modifier.offset(2.dp)
                                .size(MaterialTheme.typography.bodyLarge.fontSize.value.dp),
                            tint = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }

            }
        }
    )
}