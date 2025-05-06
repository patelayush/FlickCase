package org.appsmith.flickcase.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch
import org.appsmith.flickcase.ERROR_MSG
import org.appsmith.flickcase.Screen
import org.appsmith.flickcase.model.cast.CastDetailsResponse
import org.appsmith.flickcase.model.configuration.ConfigurationResponse
import org.appsmith.flickcase.model.genre.Genre
import org.appsmith.flickcase.model.moviedetails.MovieDetailsResponse
import org.appsmith.flickcase.model.movies.Movie
import org.appsmith.flickcase.model.movies.MoviesByGenre
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
    val isLoadingAdditionalMovies = mutableStateOf(false)
    val isContentDetailsLoading = mutableStateOf(false)

    // this bool would be used to separate movies & Tv shows. Assume tvshows in false case.
    val showMovies = mutableStateOf(true)
    val nowPlayingMovies = mutableStateListOf<Movie?>()
    val nowPlayingMoviesPage = mutableStateOf(1)
    val trendingContent = mutableStateListOf<Movie?>()
    val trendingContentPage = mutableStateOf(1)
    val configuration = mutableStateOf<ConfigurationResponse?>(null)
    val countries = mutableStateOf<List<String>?>(null)
    val contentGenres = mutableStateListOf<Genre>()
    val selectedGenres = mutableStateListOf<Genre>()
    val contentByGenre = mutableStateListOf<MoviesByGenre>()
    val contentByMultipleGenre = mutableStateListOf<Movie?>()
    val searchedContent = mutableStateListOf<Movie?>(null)
    val contentDetails = mutableStateOf<MovieDetailsResponse?>(null)
    val castDetails = mutableStateOf<CastDetailsResponse?>(null)

    fun getPopularGenres(): List<String> {
        return if (showMovies.value) listOf(
            "Action",
            "Thriller",
            "Comedy",
            "Horror"
        ) else listOf(
            "Crime",
            "Reality",
            "Comedy",
            "Drama",
            "Documentary"
        )
    }

    fun init() {
        isLoading.value = true
        viewModelScope.launch {
            settings.putInt(sessionCountKey, settings.getInt(sessionCountKey, 0) + 1)
            getConfigutation()
            getCountries()
            getTrendingContent()
            getNowPlayingMovies()
            getContentGenres()
        }.invokeOnCompletion {
            isLoading.value = false
        }
    }

    fun refresh() {
        isLoading.value = true
        viewModelScope.launch {
            resetTrendingContent()
            resetNowPlayingContent()
            if (showMovies.value) {
                getNowPlayingMovies()
            }
            getTrendingContent()
            contentByGenre.clear()
            getContentGenres()
        }.invokeOnCompletion {
            isLoading.value = false
        }
    }

    private fun resetNowPlayingContent() {
        nowPlayingMovies.clear()
        nowPlayingMoviesPage.value = 1
    }

    private fun resetTrendingContent() {
        trendingContent.clear()
        trendingContentPage.value = 1
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

    suspend fun getTrendingContent() {
        client.getTrendingContent(
            page = trendingContentPage.value,
            forMovies = showMovies.value,
            region = selectedRegion.value
        )
            .onSuccess { response ->
                response?.let {
                    if (it.results?.isEmpty() == true && it.page >= it.total_pages) {
                        errorMessage.value = ERROR_MSG.REACHED_END.msg
                    }
                    trendingContent.addAll(it.results ?: listOf())
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
        client.getNowPlayingMovies(page = nowPlayingMoviesPage.value, region = selectedRegion.value)
            .onSuccess { response ->
                response?.let {
                    if (it.results?.isEmpty() == true && it.page >= it.total_pages) {
                        errorMessage.value = ERROR_MSG.REACHED_END.msg
                    }
                    nowPlayingMovies.addAll(it.results ?: listOf())
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getContentGenres() {
        client.getContentGenres(forMovies = showMovies.value)
            .onSuccess { response ->
                response?.let {
                    contentGenres.clear()
                    contentGenres.addAll(it.genres?.toMutableList() ?: mutableListOf())
                    getFilteredContentGenres()
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
    }

    suspend fun getMoviesByGenre(genre: Genre, page: Int = 1) {
        client.searchMoviesByGenre(
            page = page, forMovies = showMovies.value, genreId = genre.id ?: 0,
            region = selectedRegion.value
        ).onSuccess { response ->
            response?.let {
                if (contentByGenre.any { it.genre == genre }) {
                    contentByGenre.firstOrNull { it.genre == genre }?.movies?.addAll(
                        it.results ?: listOf()
                    )
                } else {
                    contentByGenre.add(
                        MoviesByGenre(
                            genre = genre,
                            movies = it.results?.toMutableList() ?: mutableListOf()
                        )
                    )
                }
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
                forMovies = showMovies.value,
                genres = selectedGenres.map { it.id ?: 0 },
                region = selectedRegion.value
            )
                .onSuccess { response ->
                    response?.let {
                        contentByMultipleGenre.clear()
                        contentByMultipleGenre.addAll(response.results ?: listOf())
                    } ?: run {
                        errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                    }
                }.onError {
                    errorMessage.value = ERROR_MSG.API_ERROR.msg
                }
        }
    }

    fun searchContent(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            searchedContent.clear()
            client.searchContent(
                forMovies = true,
                query = query,
                region = selectedRegion.value
            ).onSuccess { response ->
                response?.let { movieResponse ->
                    client.searchContent(
                        forMovies = false,
                        query = query,
                        region = selectedRegion.value
                    ).onSuccess { tvResponse ->
                        val movies = movieResponse.results?.filter { it?.vote_average != 0.0 }
                            ?.toMutableList() ?: mutableListOf()
                        movies.addAll(
                            tvResponse?.results?.filter { it?.vote_average != 0.0 } ?: listOf())
                        searchedContent.addAll(movies.shuffled())
                    }.onError {
                        errorMessage.value = ERROR_MSG.API_ERROR.msg
                    }
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

    fun getContent(contentId: Int?) {
        viewModelScope.launch {
            isContentDetailsLoading.value = true
            getCastDetails(contentId)
            getContentDetails(contentId)
        }.invokeOnCompletion {
            isContentDetailsLoading.value = false
        }
    }

    suspend fun getContentDetails(movieId: Int?) {
        contentDetails.value = null
        movieId?.let {
            client.getContentDetails(
                forMovies = showMovies.value,
                movieId = it
            ).onSuccess { response ->
                response?.let {
                    contentDetails.value = it
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
        }
    }

    suspend fun getCastDetails(movieId: Int?) {
        castDetails.value = null
        movieId?.let {
            client.getCast(
                forMovies = showMovies.value,
                contentId = it
            ).onSuccess { response ->
                response?.let {
                    castDetails.value = it
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
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

    suspend fun getFilteredContentGenres() {
        contentByGenre.clear()
        val popularGenres =
            contentGenres.filter { getPopularGenres().contains(it.name) }
        popularGenres.forEach {
            getMoviesByGenre(it)
        }
    }

    fun loadMoreMovies(category: String? = null) {
        viewModelScope.launch {
            isLoadingAdditionalMovies.value = true
            if (category?.contains("Trending") == true) {
                trendingContentPage.value++
                getTrendingContent()
            } else if (category?.contains("In Theatres") == true) {
                nowPlayingMoviesPage.value++
                getNowPlayingMovies()
            } else {
                contentByGenre.firstOrNull {
                    category?.contains(
                        it.genre.name ?: "default"
                    ) == true
                }
                    ?.let {
                        it.page++
                        getMoviesByGenre(it.genre, it.page)
                    }
            }
        }.invokeOnCompletion {
            isLoadingAdditionalMovies.value = false
        }
    }
}

