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
    val similarContent = mutableStateListOf<Movie?>()
    val similarContentPage = mutableStateOf(1)
    val configuration = mutableStateOf<ConfigurationResponse?>(null)
    val countries = mutableStateOf<List<String>?>(null)
    val contentGenres = mutableStateListOf<Genre>()
    val selectedGenres = mutableStateListOf<Genre>()
    val contentByGenre = mutableStateListOf<MoviesByGenre>()
    val contentByMultipleGenre = mutableStateListOf<Movie?>()
    val contentByMultipleGenrePage = mutableStateOf(1)
    val reachedEndOfMoviesByMultipleGenre = mutableStateOf(false)
    val searchedContent = mutableStateListOf<Movie?>(null)
    val searchedContentMoviePage = mutableStateOf(1)
    val searchedContentTvPage = mutableStateOf(1)
    val reachedEndOfSearchedContent = mutableStateOf(false)
    val contentDetails = mutableStateOf<MovieDetailsResponse?>(null)
    val castDetails = mutableStateOf<CastDetailsResponse?>(null)

    var openMovieDetailSheet = mutableStateOf(false)
    var showRegionSelector = mutableStateOf(false)

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
                    if (it.results?.isEmpty() == true && it.page >= it.total_pages && it.page != 1) {
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
            isLoading.value = true
            client.searchMoviesByMutlipleGenres(
                page = contentByMultipleGenrePage.value,
                forMovies = showMovies.value,
                genres = selectedGenres.map { it.id ?: 0 },
                region = selectedRegion.value
            ).onSuccess { response ->
                response?.let {
                    if (it.results?.isEmpty() == true || it.page >= it.total_pages) {
                        reachedEndOfMoviesByMultipleGenre.value = true
                    }
                    contentByMultipleGenre.addAll(response.results ?: listOf())
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

    fun searchContent(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            client.searchContent(
                page = searchedContentMoviePage.value,
                forMovies = true,
                query = query,
                region = selectedRegion.value
            ).onSuccess { response ->
                response?.let { movieResponse ->
                    client.searchContent(
                        page = searchedContentTvPage.value,
                        forMovies = false,
                        query = query,
                        region = selectedRegion.value
                    ).onSuccess { tvResponse ->
                        val movies = movieResponse.results?.filter { it?.vote_average != 0.0 }
                            ?.toMutableList() ?: mutableListOf()
                        val tvshows =
                            tvResponse?.results?.filter { it?.vote_average != 0.0 } ?: listOf()
                        movies.addAll(tvshows)
                        if (movies.isEmpty() && tvshows.isEmpty()) {
                            reachedEndOfSearchedContent.value = true
                        }
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


    fun resetSearchContentState() {
        searchedContentMoviePage.value = 1
        searchedContentTvPage.value = 1
        searchedContent.clear()
    }

    fun loadMoreSearchedContent(query: String) {
        searchedContentMoviePage.value++
        searchedContentTvPage.value++
        searchContent(query = query)
    }

    fun getContent(contentId: Int?) {
        viewModelScope.launch {
            openMovieDetailSheet.value = true
            isContentDetailsLoading.value = true
            getCastDetails(contentId)
            similarContent.clear()
            getSimilarContent(contentId)
            getContentDetails(contentId)
        }.invokeOnCompletion {
            isContentDetailsLoading.value = false
        }
    }

    suspend fun getSimilarContent(contentId: Int?) {
        contentId?.let {
            client.getSimilarContent(
                forMovies = showMovies.value,
                contentId = it
            ).onSuccess { response ->
                response?.let { result ->
                    if (result.results?.isEmpty() == true && (result.page >= result.total_pages) && result.page != 1) {
                        errorMessage.value = ERROR_MSG.REACHED_END.msg
                    }
                    similarContent.addAll(result.results ?: listOf())
                } ?: run {
                    errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                }
            }.onError {
                errorMessage.value = ERROR_MSG.API_ERROR.msg
            }
        }
    }

    suspend fun getContentDetails(movieId: Int?) {
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

    fun loadMoreSimilarMovies() {
        viewModelScope.launch {
            similarContentPage.value++
            getSimilarContent(contentDetails.value?.id)
        }
    }

    fun loadMoreMoviesByMultipleGenres() {
        contentByMultipleGenrePage.value++
        getMoviesByMultipleGenres()
    }

    fun resetContentByMultipleGenre() {
        contentByMultipleGenre.clear()
        contentByMultipleGenrePage.value = 1
        reachedEndOfMoviesByMultipleGenre.value = false
    }
}

