package org.appsmith.filmestry.model.nowplayingmovies

import kotlinx.serialization.Serializable

@Serializable
data class Dates(
    val maximum: String? = null,
    val minimum: String? = null
)