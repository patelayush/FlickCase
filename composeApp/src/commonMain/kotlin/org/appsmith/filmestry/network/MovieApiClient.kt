package org.appsmith.filmestry.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.appsmith.filmestry.model.AuthenticationResponse
import org.appsmith.filmestry.model.TrendingMovieResponse
import org.appsmith.filmestry.network.util.NetworkError
import org.appsmith.filmestry.network.util.Result
import org.appsmith.filmestry.network.util.result
import org.appsmith.filmestry.tmdbApiHost

class MovieApiClient(
    private val httpClient: HttpClient
) {
    suspend fun authenticate(): Result<AuthenticationResponse, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/authentication") {
                contentType(ContentType.Application.Json)
            }
            result<AuthenticationResponse>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getTrendingMovies(language: String = "en-US"): Result<TrendingMovieResponse?, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/trending/movie/week?language=$language")
            result<TrendingMovieResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    private fun checkForError(e: Exception): Result.Error<NetworkError> =
        if (e.message?.contains("Unable to resolve host") == true) {
            Result.Error(NetworkError.NO_INTERNET)
        } else {
            println(e.message)
            Result.Error(NetworkError.UNKNOWN)
        }

}