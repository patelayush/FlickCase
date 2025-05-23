package org.appsmith.flickcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import io.ktor.client.engine.okhttp.OkHttp
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.network.createHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = this.window
            val decorView = window.decorView
            WindowCompat.getInsetsController(window, decorView).isAppearanceLightStatusBars = false
            App(
                client = remember {
                    MovieApiClient(createHttpClient(OkHttp.create()))
                }
            )
        }
    }
}