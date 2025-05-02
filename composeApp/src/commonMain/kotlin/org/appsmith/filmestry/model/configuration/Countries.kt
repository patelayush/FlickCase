package org.appsmith.filmestry.model.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Countries(
    val english_name: String? = null,
    val iso_3166_1: String? = null,
    val native_name: String? = null
)