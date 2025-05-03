package org.appsmith.flickcase.model.nowplayingmovies

import kotlinx.serialization.Serializable

@Serializable
data class Dates(
    val maximum: String? = null,
    val minimum: String? = null
)