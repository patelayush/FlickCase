package org.appsmith.flickcase.model.nowplayingmovies

import kotlinx.serialization.Serializable
import org.appsmith.flickcase.model.movies.Movie

@Serializable
data class NowPlayingMoviesResponse(
    val dates: Dates? = null,
    val page: Int? = null,
    val results: List<Movie?>? = null,
    val total_pages: Int? = null,
    val total_results: Int? = null
)