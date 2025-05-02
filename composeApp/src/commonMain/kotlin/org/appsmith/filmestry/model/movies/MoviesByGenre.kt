package org.appsmith.filmestry.model.movies

import org.appsmith.filmestry.model.genre.Genre

data class MoviesByGenre(
    val genre: Genre,
    val movies: List<Movie?>?,
)
