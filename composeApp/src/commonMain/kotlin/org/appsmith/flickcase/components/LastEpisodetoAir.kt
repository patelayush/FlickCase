package org.appsmith.flickcase.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse

@Composable
fun LastEpisodeToAirSection(
    modifier: Modifier,
    tvShow: MovieDetailsResponse?,
    baseUrl: String? = null,
    seasonOverview: String? = null
) {
    var height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    if (tvShow?.last_episode_to_air != null || tvShow?.next_episode_to_air != null) {
        Text(
            text = if (tvShow.next_episode_to_air != null) "Current Season" else "Last Season",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier,
        )
        val show = tvShow.next_episode_to_air ?: tvShow.last_episode_to_air
        Card(
            modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            elevation = CardDefaults.elevatedCardElevation(10.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Row {
                FlickCaseImageLoader(
                    modifier = Modifier.width(120.dp).heightIn(min = 60.dp, max = height),
                    baseUrl = baseUrl ?: "",
                    path = tvShow.next_episode_to_air?.still_path?.takeIf { it.isNotBlank() }
                        ?: tvShow.last_episode_to_air?.still_path ?: "",
                    shape = RectangleShape
                )
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1f)
                        .onSizeChanged {
                            density.run {
                                height = it.height.toDp() + 45.dp
                            }
                        },
                ) {
                    Text(
                        text = "Season ${show?.season_number ?: ""}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row {
                        if (show?.air_date != null) {
                            Text(
                                text = show.air_date.takeIf { it.isNotBlank() }
                                    ?.substring(startIndex = 0, endIndex = 4) ?: "",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = " | ",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (tvShow?.seasons?.lastOrNull()?.episode_count != null) {
                            Text(
                                text = tvShow.seasons.lastOrNull()?.episode_count.toString() + " episodes",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    ExpandableText(
                        text = seasonOverview ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }
    }
}