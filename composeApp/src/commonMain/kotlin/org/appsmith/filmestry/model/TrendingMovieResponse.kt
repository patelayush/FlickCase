package org.appsmith.filmestry.model

import kotlinx.serialization.Serializable

@Serializable
data class TrendingMovieResponse(
    val page: Int? = null,
    val results: List<Movie?>?= null,
    val total_pages: Int?= null,
    val total_results: Int?= null
)