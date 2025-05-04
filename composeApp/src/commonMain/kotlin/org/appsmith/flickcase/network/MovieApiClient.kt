package org.appsmith.flickcase.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.appsmith.flickcase.model.authentication.AuthenticationResponse
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.configuration.Countries
import org.appsmith.flickcase.model.genre.GenresResponse
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse
import org.appsmith.flickcase.model.movies.MoviesResponse
import org.appsmith.flickcase.model.nowplayingmovies.NowPlayingMoviesResponse
import org.appsmith.flickcase.network.util.NetworkError
import org.appsmith.flickcase.network.util.Result
import org.appsmith.flickcase.network.util.result
import org.appsmith.flickcase.tmdbApiHost

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

    suspend fun getTrendingContent(
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US"
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get(
                    if (forMovies) "$tmdbApiHost/trending/movie/week?language=$language&region=$region"
                    else "$tmdbApiHost/trending/tv/day?language=$language&region=$region"
                )
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

    suspend fun getContentGenres(forMovies: Boolean): Result<GenresResponse?, NetworkError> {
        return try {
            val response = httpClient.get(
                if (forMovies) "$tmdbApiHost/genre/movie/list"
                else "$tmdbApiHost/genre/tv/list"
            )
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
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US",
        genreId: Int
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response = httpClient.get(
                if (forMovies) "$tmdbApiHost/discover/movie?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc&with_genres=$genreId"
                else "$tmdbApiHost/discover/tv?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc&with_genres=$genreId"
            )
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun searchMoviesByMutlipleGenres(
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US",
        genres: List<Int>
    ): Result<MoviesResponse?, NetworkError> {
        val genreString = genres.joinToString(",")
        return try {
            val response = httpClient.get(
                if(forMovies) "$tmdbApiHost/discover/movie?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc&with_genres=$genreString"
                else "$tmdbApiHost/discover/tv?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc&with_genres=$genreString"
            )
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun searchContent(
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US",
        query: String
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response = httpClient.get(
                if(forMovies) "$tmdbApiHost/search/movie?query=$query&include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc"
                else "$tmdbApiHost/search/tv?query=$query&include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=1&sort_by=vote_count.desc"
            )
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getContentDetails(
        forMovies: Boolean,
        movieId: Int?
    ): Result<MovieDetailsResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get("$tmdbApiHost/${if (forMovies) "movie" else "tv"}/$movieId")
            result<MovieDetailsResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getTrendingTvShows(
        language: String = "en-US",
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get("$tmdbApiHost/trending/tv/day?language=$language")
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