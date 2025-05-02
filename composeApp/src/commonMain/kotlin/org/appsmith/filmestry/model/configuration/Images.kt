package org.appsmith.filmestry.model.configuration
import kotlinx.serialization.Serializable

@Serializable
data class Images(
    val backdrop_sizes: List<String?>? = null,
    val base_url: String? = null,
    val logo_sizes: List<String?>? = null,
    val poster_sizes: List<String?>? = null,
    val profile_sizes: List<String?>? = null,
    val secure_base_url: String? = null,
    val still_sizes: List<String?>? = null
)