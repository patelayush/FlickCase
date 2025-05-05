package org.appsmith.flickcase.model.nowplayingmovies

import kotlinx.serialization.Serializable
import org.appsmith.flickcase.model.movies.Movie

@Serializable
data class NowPlayingMoviesResponse(
    val dates: Dates? = null,
    val page: Int = 0,
    val results: List<Movie?>? = null,
    val total_pages: Int = 0,
    val total_results: Int? = null
)