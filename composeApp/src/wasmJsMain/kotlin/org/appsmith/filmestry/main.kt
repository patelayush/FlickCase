package org.appsmith.filmestry

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.ktor.client.engine.js.Js
import kotlinx.browser.document
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.network.createHttpClient

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        App(
            client = remember {
                MovieApiClient(createHttpClient(Js.create()))
            }
        )
    }
}