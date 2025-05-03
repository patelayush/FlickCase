package org.appsmith.flickcase.model.movies

import org.appsmith.flickcase.model.genre.Genre

data class MoviesByGenre(
    val genre: Genre,
    val movies: List<Movie?>?,
)
