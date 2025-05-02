package org.appsmith.filmestry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.appsmith.filmestry.model.genre.Genre


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreSection(
    modifier: Modifier = Modifier,
    genres: List<Genre?>,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
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
    genres: List<Genre?>,
    onGenreSelected: (Genre) -> Unit = {},
    onGenreRemoved: (Genre) -> Unit = {}
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(genres) { genre ->
            GenreChip(
                genre = genre,
                genreTextStyle = MaterialTheme.typography.bodyLarge,
                generateRandomColor = false,
                onGenreSelected = onGenreSelected,
                onGenreRemoved = onGenreRemoved
            )
        }
    }
}

@Composable
fun GenreChip(
    genre: Genre?,
    generateRandomColor: Boolean = true,
    genreTextStyle:TextStyle = MaterialTheme.typography.labelMedium,
    onGenreSelected: (Genre) -> Unit = {},
    onGenreRemoved: (Genre) -> Unit = {}
) {
    var genreSelected by rememberSaveable { mutableStateOf(false) }
    val skillColorSet =
        if (generateRandomColor) getGenreColorSet()
        else {
            if (genreSelected) Pair(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer
            )
            else Pair(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    genre?.let {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = skillColorSet.first,
            ),
            onClick = {
                genreSelected = !genreSelected
                if (genreSelected) onGenreSelected(genre)
                else onGenreRemoved(genre)
            }
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
                    color = skillColorSet.second
                )

                if (genreSelected) {
                    Icon(
                        Icons.Default.Close,
                        modifier = Modifier.size(genreTextStyle.fontSize.value.dp),
                        contentDescription = "selected",
                        tint = skillColorSet.second,
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