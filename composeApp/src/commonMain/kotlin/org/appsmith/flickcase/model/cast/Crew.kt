package org.appsmith.flickcase.model.cast

import kotlinx.serialization.Serializable

@Serializable
data class Crew(
    val adult: Boolean? = null,
    val credit_id: String? = null,
    val department: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val job: String? = null,
    val known_for_department: String? = null,
    val name: String? = null,
    val original_name: String? = null,
    val popularity: Double? = null,
    val profile_path: String? = null
)