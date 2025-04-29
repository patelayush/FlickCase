package org.appsmith.filmestry

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.engine.darwin.Darwin
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.network.createHttpClient

fun MainViewController() = ComposeUIViewController {
    App(
        client = remember {
            MovieApiClient(createHttpClient(Darwin.create()))
        }
    )
}