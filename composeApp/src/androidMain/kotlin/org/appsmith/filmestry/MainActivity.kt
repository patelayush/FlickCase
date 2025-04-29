package org.appsmith.filmestry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import io.ktor.client.engine.okhttp.OkHttp
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.network.createHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = this.window
            val decorView = window.decorView
            WindowCompat.getInsetsController(window, decorView).isAppearanceLightStatusBars =
                !isSystemInDarkTheme()
            App(
                client = remember {
                    MovieApiClient(createHttpClient(OkHttp.create()))
                }
            )
        }
    }
}