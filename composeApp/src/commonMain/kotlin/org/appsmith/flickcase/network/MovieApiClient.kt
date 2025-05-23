package org.appsmith.flickcase.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.appsmith.flickcase.model.authentication.AuthenticationResponse
import org.appsmith.flickcase.model.cast.CastDetailsResponse
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
        page:Int,
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US"
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get(
                    if (forMovies) "$tmdbApiHost/trending/movie/day?language=$language&region=$region&page=$page"
                    else "$tmdbApiHost/trending/tv/day?language=$language&region=$region&page=$page"
                )
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun getNowPlayingMovies(
        page: Int,
        language: String = "en-US",
        region: String = "US"
    ): Result<NowPlayingMoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get("$tmdbApiHost/movie/now_playing?language=$language&region=$region&page=$page")
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

    suspend fun getCast(forMovies: Boolean, contentId: Int?): Result<CastDetailsResponse?, NetworkError> {
        return try {
            val response = httpClient.get(
                if (forMovies) "$tmdbApiHost/movie/$contentId/credits"
                else "$tmdbApiHost/tv/$contentId/credits"
            )
            result<CastDetailsResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun searchMoviesByGenre(
        page: Int,
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US",
        genreId: Int
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response = httpClient.get(
                if (forMovies) "$tmdbApiHost/discover/movie?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=$page&sort_by=vote_count.desc&with_genres=$genreId"
                else "$tmdbApiHost/discover/tv?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=$page&sort_by=vote_count.desc&with_genres=$genreId"
            )
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun searchMoviesByMutlipleGenres(
        page:Int = 1,
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US",
        genres: List<Int>
    ): Result<MoviesResponse?, NetworkError> {
        val genreString = genres.joinToString(",")
        return try {
            val response = httpClient.get(
                if(forMovies) "$tmdbApiHost/discover/movie?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=$page&sort_by=vote_count.desc&with_genres=$genreString"
                else "$tmdbApiHost/discover/tv?include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=$page&sort_by=vote_count.desc&with_genres=$genreString"
            )
            result<MoviesResponse?>(response)
        } catch (e: Exception) {
            checkForError(e)
        }
    }

    suspend fun searchContent(
        page: Int = 1,
        forMovies: Boolean = true,
        language: String = "en-US",
        region: String = "US",
        query: String
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response = httpClient.get(
                if(forMovies) "$tmdbApiHost/search/movie?query=$query&include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=$page&sort_by=vote_count.desc"
                else "$tmdbApiHost/search/tv?query=$query&include_adult=false&include_video=false&region=$region&language=$language&with_origin_country=$region&page=$page&sort_by=vote_count.desc"
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

    suspend fun getSimilarContent(
        page:Int = 1,
        forMovies: Boolean,
        contentId: Int?
    ): Result<MoviesResponse?, NetworkError> {
        return try {
            val response =
                httpClient.get("$tmdbApiHost/${if (forMovies) "movie" else "tv"}/$contentId/similar?page=$page")
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