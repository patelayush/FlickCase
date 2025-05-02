package org.appsmith.filmestry.model.genre
import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    val genres: List<Genre>? = null
)
@Serializable
data class Genre(
    val id: Int? = null,
    val name: String? = null
)