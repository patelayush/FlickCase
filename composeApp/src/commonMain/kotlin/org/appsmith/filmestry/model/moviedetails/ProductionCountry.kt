package org.appsmith.filmestry.model.moviedetails
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountry(
    val iso_3166_1: String? = null,
    val name: String? = null
)