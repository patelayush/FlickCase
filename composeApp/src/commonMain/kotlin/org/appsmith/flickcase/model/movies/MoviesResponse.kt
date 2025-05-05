package org.appsmith.flickcase.model.movies

import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
    val page: Int = 0,
    val results: List<Movie?>?= null,
    val total_pages: Int= 0,
    val total_results: Int?= null
)