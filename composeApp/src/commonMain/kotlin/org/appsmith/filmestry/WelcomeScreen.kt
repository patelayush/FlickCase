package org.appsmith.filmestry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.app_icon
import filmestry.composeapp.generated.resources.ic_start
import kotlinx.coroutines.delay
import org.appsmith.filmestry.components.AppButton
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    var isTitleVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isTitleVisible = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center).animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.app_icon),
                modifier = Modifier.size(100.dp),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Welcome to",
                lineHeight = 1.em,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.displayLarge
            )

            AnimatedVisibility(
                visible = isTitleVisible,
                enter = fadeIn(tween(durationMillis = 500))
            ) {
                Column {
                    Text(
                        text = "Filmestry",
                        letterSpacing = 1.8.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge
                    )

                    AppButton(
                        modifier = Modifier
                            .padding(vertical = 30.dp)
                            .align(Alignment.End),
                        text = "Let's go",
                        onClick = {
                            TODO()
                        },
                        icon = Res.drawable.ic_start
                    )
                }
            }
        }
    }
}

