package org.appsmith.filmestry.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.appsmith.filmestry.network.util.NetworkError
import org.appsmith.filmestry.network.util.Result
import org.appsmith.filmestry.tmdbApiHost

class MovieApiClient(
    private val httpClient: HttpClient
) {

    suspend fun authenticate(): Result<Boolean, NetworkError> {
       try {
            httpClient.get("$tmdbApiHost/authentication")
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }
    }
}