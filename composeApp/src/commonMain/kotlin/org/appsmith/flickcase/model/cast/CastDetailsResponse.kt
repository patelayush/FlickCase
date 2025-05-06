package org.appsmith.flickcase.model.cast

import kotlinx.serialization.Serializable

@Serializable
data class CastDetailsResponse(
    val cast: List<Cast?>? = null,
    val crew: List<Crew?>? = null,
    val id: Int? = null
)