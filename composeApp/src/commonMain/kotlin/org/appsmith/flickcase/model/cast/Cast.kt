package org.appsmith.flickcase.model.cast

import kotlinx.serialization.Serializable

@Serializable
data class Cast(
    val adult: Boolean? = null,
    val cast_id: Int? = null,
    val character: String? = null,
    val credit_id: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val known_for_department: String? = null,
    val name: String? = null,
    val order: Int? = null,
    val original_name: String? = null,
    val popularity: Double? = null,
    val profile_path: String? = null
)