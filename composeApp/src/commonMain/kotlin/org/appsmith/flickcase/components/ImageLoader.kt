package org.appsmith.flickcase.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun FlickCaseImageLoader(
    modifier: Modifier = Modifier,
    baseUrl: String,
    path: String,
    voteAverage: Int? = null,
    additionalLoader: Boolean = false,
) {
    var isImageLoading by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = "${baseUrl}original${path}",
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
            modifier = Modifier.clip(
                RoundedCornerShape(15.dp)
            ),
        )

        if (isImageLoading || additionalLoader) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSurface,
                strokeWidth = 5.dp,
                modifier = Modifier.size(24.dp)
            )
        }

        if (voteAverage != null) {
            Text(
                text = "$voteAverage%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .wrapContentWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 5.dp, vertical = 3.dp)
            )
        }
    }

}