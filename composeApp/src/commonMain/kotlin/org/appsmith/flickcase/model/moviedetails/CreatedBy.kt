package org.appsmith.flickcase.model.moviedetails
import kotlinx.serialization.Serializable

@Serializable
data class CreatedBy(
    val credit_id: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val original_name: String? = null,
    val profile_path: String? = null
)