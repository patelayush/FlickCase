package org.appsmith.filmestry.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.appsmith.filmestry.ERROR_MSG
import org.appsmith.filmestry.Screen
import org.appsmith.filmestry.model.TrendingMovieResponse
import org.appsmith.filmestry.network.MovieApiClient
import org.appsmith.filmestry.network.util.onError
import org.appsmith.filmestry.network.util.onSuccess

class HomeViewModel(private val client: MovieApiClient) : ViewModel() {

    val currentScreen = mutableStateOf(Screen.Welcome)
    val errorMessage = mutableStateOf("")
    val isLoading = mutableStateOf(false)

    val trendingMovies = mutableStateOf(TrendingMovieResponse())

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

    fun getTrendingMovies() {
        viewModelScope.launch {
            isLoading.value = true
            client.getTrendingMovies()
                .onSuccess { response ->
                    response?.let {
                        trendingMovies.value = it
                    } ?: run {
                        errorMessage.value = ERROR_MSG.EMPTY_RESPONSE.msg
                    }
                }
                .onError {
                    errorMessage.value = ERROR_MSG.API_ERROR.msg
                }
                .also {
                    isLoading.value = false
                }
        }
    }
}

