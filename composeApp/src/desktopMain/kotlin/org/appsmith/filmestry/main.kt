package org.appsmith.filmestry

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.plugins.logging.LoggingFormat
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.network.createHttpClient

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Filmestry",
    ) {
        App(
            client = remember {
                MovieApiClient(createHttpClient(LoggingFormat.OkHttp.create()))
            }
        )
    }
}