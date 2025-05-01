package org.appsmith.filmestry.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import filmestry.composeapp.generated.resources.Res
import filmestry.composeapp.generated.resources.app_icon
import filmestry.composeapp.generated.resources.ic_start
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.appsmith.filmestry.APP_NAME
import org.appsmith.filmestry.Screen
import org.appsmith.filmestry.components.AppButton
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    client: MovieApiClient,
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
                .fillMaxWidth()
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
                lineHeight = 1.em,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.displayLarge
            )

            AnimatedVisibility(
                visible = isTitleVisible,
                enter = fadeIn(tween(durationMillis = 500))
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = APP_NAME,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        letterSpacing = 1.8.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge
                    )

                    AppButton(
                        modifier = Modifier
                            .padding(vertical = 30.dp)
                            .align(Alignment.End)
                            .padding(end = 30.dp),
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

