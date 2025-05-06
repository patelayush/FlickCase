package org.appsmith.flickcase.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.appsmith.flickcase.model.genre.Genre


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreSection(
    modifier: Modifier = Modifier,
    genres: List<Genre?>,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        genres.forEach { genre ->
            GenreChip(
                genre = genre
            )
        }
    }
}

@Composable
fun GenreRow(
    modifier: Modifier = Modifier,
    genres: SnapshotStateList<Genre>,
    selectedGenres: SnapshotStateList<Genre>,
    onGenreSelected: (Genre) -> Unit = {},
    onGenreRemoved: (Genre) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 15.dp)
    ) {
        items(selectedGenres) { genre ->
            GenreChipSelectable(
                genre = genre,
                genreSelected = true,
                genreTextStyle = MaterialTheme.typography.bodyLarge,
                onGenreRemoved = {
                    val selectedGenresCopy = selectedGenres - it
                    selectedGenres.clear()
                    selectedGenres.addAll(selectedGenresCopy)
                    onGenreRemoved(it)
                }
            )
        }
        items(genres.filterNot { selectedGenres.contains(it) }) { genre ->
            GenreChipSelectable(
                genre = genre,
                genreSelected = false,
                genreTextStyle = MaterialTheme.typography.bodyLarge,
                onGenreSelected = {
                    val selectedGenresCopy = selectedGenres + it
                    selectedGenres.clear()
                    selectedGenres.addAll(selectedGenresCopy)
                    onGenreSelected(it)
                },
            )
        }
    }
}

@Composable
fun GenreChip(
    genre: Genre?,
    genreTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
) {
    val genreColorSet = getGenreColorSet()
    genre?.let {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = genreColorSet.first,
            ),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = genreTextStyle,
                    text = genre.name ?: "",
                    modifier = Modifier,
                    color = genreColorSet.second
                )
            }
        }
    }
}

@Composable
fun GenreChipSelectable(
    genre: Genre?,
    genreSelected: Boolean = false,
    genreTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    onGenreSelected: (Genre) -> Unit = {},
    onGenreRemoved: (Genre) -> Unit = {}
) {
    val genreColorSet = Pair(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.onPrimaryContainer
    )
    genre?.let {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
            onClick = {
                if (genreSelected) onGenreRemoved(genre)
                else onGenreSelected(genre)
            },
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .then(
                        if(genreSelected){
                            Modifier.background(
                                color = genreColorSet.first
                            )
                        } else
                            Modifier
                                .background(Color.Transparent)
                                .border(
                                    2.dp,
                                    genreColorSet.first,
                                    RoundedCornerShape(20.dp)
                                )

                    ).padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = genreTextStyle,
                    text = genre.name ?: "",
                    modifier = Modifier,
                    color = if(genreSelected) genreColorSet.second else MaterialTheme.colorScheme.onSurface
                )

                if (genreSelected) {
                    Icon(
                        Icons.Default.Close,
                        modifier = Modifier.size(genreTextStyle.fontSize.value.dp),
                        contentDescription = "selected",
                        tint = genreColorSet.second,
                    )
                }
            }
        }
    }
}

@Composable
fun getGenreColorSet(): Pair<Color, Color> {
    val skillColorSet: Set<Pair<Color, Color>> = setOf(
        Pair(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer
        ),
        Pair(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer
        ),
        Pair(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        ),
    )
    return skillColorSet.random()
}