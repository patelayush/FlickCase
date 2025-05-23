package org.appsmith.flickcase.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import flickcase.composeapp.generated.resources.Res
import flickcase.composeapp.generated.resources.app_icon
import flickcase.composeapp.generated.resources.ic_start
import kotlinx.coroutines.delay
import org.appsmith.flickcase.APP_NAME
import org.appsmith.flickcase.components.AppButton
import org.appsmith.flickcase.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    var isTitleVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isTitleVisible = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .align(Alignment.Center)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.app_icon),
                modifier = Modifier.size(100.dp),
                contentDescription = "Logo",
            )

            Text(
                text = "Welcome to",
                letterSpacing = 0.1.em,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.displayMedium
            )

            Text(
                text = "$APP_NAME!",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = 0.1.em,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.displayMedium
            )

            AnimatedVisibility(
                visible = isTitleVisible,
                enter = fadeIn(tween(durationMillis = 500))
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Where Every Flick Has a Place.",
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    AppButton(
                        modifier = Modifier
                            .padding(vertical = 30.dp)
                            .align(Alignment.End),
                        text = "Let's go",
                        isLoading = homeViewModel.isLoading.value,
                        onClick = {
                            homeViewModel.authenticate()
                        },
                        icon = Res.drawable.ic_start
                    )
                }
            }
        }
    }
}


