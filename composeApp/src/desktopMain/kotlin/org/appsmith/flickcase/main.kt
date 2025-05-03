package org.appsmith.flickcase

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.engine.okhttp.OkHttp
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.network.createHttpClient
import org.appsmith.flickcase.shared.DATA_STORE_FILE_NAME
import org.appsmith.flickcase.shared.createDataStore

fun main() = application {
    val prefs = createDataStore {
        DATA_STORE_FILE_NAME
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "FlickCase",
    ) {
        App(
            client = remember {
                MovieApiClient(createHttpClient(OkHttp.create()))
            }
        )
    }
}