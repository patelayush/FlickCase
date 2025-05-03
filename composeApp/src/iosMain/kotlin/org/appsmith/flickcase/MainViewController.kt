package org.appsmith.flickcase

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.engine.darwin.Darwin
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.network.createHttpClient

fun MainViewController() = ComposeUIViewController {
    App(
        client = remember {
            MovieApiClient(createHttpClient(Darwin.create()))
        }
    )
}