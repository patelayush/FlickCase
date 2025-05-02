package org.appsmith.filmestry

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.engine.okhttp.OkHttp
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.network.createHttpClient
import org.appsmith.filmestry.shared.DATA_STORE_FILE_NAME
import org.appsmith.filmestry.shared.createDataStore

fun main() = application {
    val prefs = createDataStore {
        DATA_STORE_FILE_NAME
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Filmestry",
    ) {
        App(
            client = remember {
                MovieApiClient(createHttpClient(OkHttp.create()))
            }
        )
    }
}