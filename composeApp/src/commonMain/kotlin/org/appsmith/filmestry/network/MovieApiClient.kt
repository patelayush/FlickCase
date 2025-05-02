package org.appsmith.filmestry.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.appsmith.filmestry.model.authentication.AuthenticationResponse
import org.appsmith.filmestry.model.configuration.ConfigurationResponse
import org.appsmith.filmestry.model.configuration.Countries
import org.appsmith.filmestry.model.genre.GenresResponse
import org.appsmith.filmestry.model.nowplayingmovies.NowPlayingMoviesResponse
import org.appsmith.filmestry.model.movies.MoviesResponse
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

    suspend fun getTrendingMovies(
        language: String = "en-US",
        region: String = "US"
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get("$tmdbApiHost/trending/movie/week?language=$language&region=$region")
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getNowPlayingMovies(
        language: String = "en-US",
        region: String = "US"
    ): Result<NowPlayingMoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get("$tmdbApiHost/movie/now_playing?language=$language&region=$region")
            result<NowPlayingMoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getConfiguration(): Result<ConfigurationResponse?, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/configuration")
            result<ConfigurationResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getCountries(): Result<List<Countries>?, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/configuration/countries")
            result<List<Countries>?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getMovieGenres(): Result<GenresResponse?, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/genre/movie/list")
            result<GenresResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getTVGenres(): Result<GenresResponse?, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/genre/tv/list")
            result<GenresResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun searchMoviesByGenre(
        language: String = "en-US",
        region: String = "US",
        genreId: Int
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response = httpClient.get("$tmdbApiHost/discover/movie?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc&with_genres=$genreId")
            result<MoviesResponse?>(response)
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