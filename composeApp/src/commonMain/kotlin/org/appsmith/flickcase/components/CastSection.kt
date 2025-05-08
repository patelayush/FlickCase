package org.appsmith.flickcase.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import org.appsmith.flickcase.model.cast.Cast

@Composable
fun CastSection(
    modifier: Modifier,
    cast: List<Cast?>?,
    baseUrl: String?=null
){
    var height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Text(
        text = "Cast",
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(horizontal = 15.dp),
    )

    Row(
        modifier = Modifier.padding(top = 10.dp)
            .horizontalScroll(rememberScrollState())
            .heightIn(min = height),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.Top
    ) {
        cast?.forEachIndexed { index, it ->
            Column(
                modifier = Modifier
                    .padding(
                        start = if(index == 0) 15.dp else 0.dp,
                        end = if(index == cast.lastIndex) 15.dp else 0.dp
                    )
                    .widthIn(max = 140.dp)
                    .onSizeChanged {
                        density.run {
                            height = max(height, it.height.toDp())
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlickCaseImageLoader(
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                    baseUrl = baseUrl ?: "",
                    path = it?.profile_path ?: "",
                )
                Text(
                    text = it?.name ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "as ${it?.character ?: ""}",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}