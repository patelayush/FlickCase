package org.appsmith.flickcase.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch
import org.appsmith.flickcase.ERROR_MSG
import org.appsmith.flickcase.Screen
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.genre.Genre
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse
import org.appsmith.flickcase.model.movies.Movie
import org.appsmith.flickcase.model.movies.MoviesByGenre
import org.appsmith.flickcase.model.movies.MoviesResponse
import org.appsmith.flickcase.model.nowplayingmovies.NowPlayingMoviesResponse
import org.appsmith.flickcase.network.MovieApiClient
import org.appsmith.flickcase.network.util.onError
import org.appsmith.flickcase.network.util.onSuccess
import org.appsmith.flickcase.regionKey
import org.appsmith.flickcase.sessionCountKey

class HomeViewModel(private val client: MovieApiClient) : ViewModel() {

    val settings = Settings()
    var selectedRegion = mutableStateOf(getSavedRegion())

    val currentScreen = mutableStateOf(if (hasSeenWelcomeScreen()) Screen.Home else Screen.Welcome)
    val errorMessage = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val isMovieDetailsLoading = mutableStateOf(false)

    val nowPlayingMovies = mutableStateOf<NowPlayingMoviesResponse?>(null)
    val trendingMovies = mutableStateOf<MoviesResponse?>(null)
    val configuration = mutableStateOf<ConfigurationResponse?>(null)
    val countries = mutableStateOf<List<String>?>(null)
    val movieGenres = mutableStateOf<List<Genre>?>(null)
    val selectedMovieGenres = mutableStateOf(mutableListOf<Genre>())
    val moviesByGenre = mutableStateOf(mutableListOf<MoviesByGenre>())
    val moviesByMultipleGenre = mutableStateOf(listOf<Movie?>())
    val searchedMovies = mutableStateOf<MoviesResponse?>(null)
    val movieDetails = mutableStateOf<MovieDetailsResponse?>(null)

    val popularGenres = listOf(
        "Action",
        "Thriller",
        "Comedy",
        "Horror"
    )

    fun init() {
        isLoading.value = true
        viewModelScope.launch {
            settings.putInt(sessionCountKey, settings.getInt(sessionCountKey, 0) + 1)
            getConfigutation()
            getCountries()
            getTrendingMovies()
            getNowPlayingMovies()
            getMovieGenres()
        }.invokeOnCompletion {
            isLoading.value = false
        }
    }

    fun refresh() {
        isLoading.value = true
        viewModelScope.launch {
            getTrendingMovies()
            getNowPlayingMovies()
            getFilteredMovieGenres()
        }.invokeOnCompletion {
            isLoading.value = false
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            isLoading.value = true
            client.authenticate()
                .onSuccess {
                    currentScreen.value = Screen.Home
                }.onError {
                    errorMessage.value = it.message
                }.also {
                    isLoading.value = false
                }
        }
    }

    suspend fun getTrendingMovies() {
        client.getTrendingMovies(region = selectedRegion.value)
            .onSuccess { response ->
                response?.let {
                    trendingMovies.value = it
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getConfigutation() {
        client.getConfiguration()
            .onSuccess { response ->
                response?.let {
                    configuration.value = it
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getCountries() {
        client.getCountries()
            .onSuccess { response ->
                response?.let {
                    countries.value = response.map { it.iso_3166_1 ?: "" }.toList()
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getNowPlayingMovies() {
        client.getNowPlayingMovies(region = selectedRegion.value)
            .onSuccess { response ->
                response?.let {
                    nowPlayingMovies.value = it
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getMovieGenres() {
        client.getMovieGenres()
            .onSuccess { response ->
                response?.let {
                    movieGenres.value = it.genres
                    getFilteredMovieGenres()
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getTVGenres() {
        client.getMovieGenres()
            .onSuccess { response ->
                response?.let {
                    movieGenres.value = it.genres
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getMoviesByGenre(genre: Genre) {
        client.searchMoviesByGenre(genreId = genre.id ?: 0, region = selectedRegion.value)
            .onSuccess { response ->
                response?.let {
                    moviesByGenre.value.add(
                        MoviesByGenre(
                            genre = genre,
                            movies = it.results ?: listOf()
                        )
                    )
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    fun getMoviesByMultipleGenres() {
        viewModelScope.launch {
            client.searchMoviesByMutlipleGenres(
                genres = selectedMovieGenres.value?.map { it.id ?: 0 } ?: listOf(), region = selectedRegion.value
            )
                .onSuccess { response ->
                    response?.let {
                        moviesByMultipleGenre.value = response.results ?: listOf()
                    } ?: run {
                        errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                    }
                }.onError {
                    errorMessage.value = ERROR_MSG.API_ERROR.msg
                }
        }
    }

    fun searchMovie(query: String) {
        viewModelScope.launch {
            searchedMovies.value = null
            isLoading.value = true
            client.searchMovie(query = query, region = selectedRegion.value)
                .onSuccess { response ->
                    response?.let {
                        searchedMovies.value = it
                    } ?: run {
                        errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                    }
                }.onError {
                    errorMessage.value = ERROR_MSG.API_ERROR.msg
                }
        }.invokeOnCompletion {
            isLoading.value = false
        }
    }

    fun getMovieDetails(movieId: Int?) {
        viewModelScope.launch {
            movieDetails.value = null
            isMovieDetailsLoading.value = true
            movieId?.let {
                client.getMovieDetails(movieId = it)
                    .onSuccess { response ->
                        response?.let {
                            movieDetails.value = it
                        } ?: run {
                            errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                        }
                    }.onError {
                        errorMessage.value = ERROR_MSG.API_ERROR.msg
                    }
            }
        }.invokeOnCompletion {
            isMovieDetailsLoading.value = false
        }
    }

    private fun hasSeenWelcomeScreen(): Boolean {
       return settings.getInt(sessionCountKey, 0) != 0
    }

    fun setSelectedRegion(region: String) {
        selectedRegion.value = region
        settings.putString(regionKey, region)
        refresh()

    }

    private fun getSavedRegion(): String {
        return settings.getStringOrNull(regionKey) ?: Locale.current.region
    }

    suspend fun getFilteredMovieGenres() {
        moviesByGenre.value.clear()
        val popularGenres =
            movieGenres.value?.filter { popularGenres.contains(it.name) } ?: listOf()
        popularGenres.forEach {
            getMoviesByGenre(it)
        }
    }
}

