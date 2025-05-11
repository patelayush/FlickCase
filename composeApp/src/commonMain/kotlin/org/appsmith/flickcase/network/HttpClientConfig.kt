package org.appsmith.flickcase.network

import org.appsmith.flickcase.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.appsmith.flickcase.network.util.NetworkError


// Docs - https://www.youtube.com/watch?v=Z1WoLYF-b14
fun createHttpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        BuildKonfig.API_KEY,
                        ""
                    )
                }
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
        install(HttpRequestRetry){
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
    }
}