package org.appsmith.flickcase.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.app_icon
import org.appsmith.flickcase.APP_NAME
import org.appsmith.flickcase.BuildKonfig
import org.jetbrains.compose.resources.painterResource

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 50.dp)
                .padding(horizontal = 20.dp)
                .widthIn(max = 650.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = "icon",
                modifier = Modifier.size(140.dp)
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = APP_NAME,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 0.1.em
                )
                Text(
                    text = "v${BuildKonfig.version}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 0.1.em
                )
            }
            Text(
                text = "Where Every Flick Has a Place.",
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "$APP_NAME is built with passion and a deep appreciation for storytelling in all its forms, aiming to provide you with an intuitive and informative way to navigate the vast landscape of film and television. Whether you're looking for the latest trending hits, eager to explore TV shows within specific genres, or searching for that particular title you've been meaning to watch, $APP_NAME has you covered. We utilize data from the TMDb API to bring you up-to-date and accurate information.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 30.dp)
            )
            Column(
                modifier = Modifier.padding(top = 30.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ){
                Text(
                    text = "Developed by:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Ayush Patel",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://patelayush.github.io/Bug-Free-Bio/")
                    }
                )
            }
            Column(
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ){
                Text(
                    text = "Credits:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "TMDb API",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://www.themoviedb.org/?language=en-US")
                    }
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
            text = "Copyright © 2025 AppSmith",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

