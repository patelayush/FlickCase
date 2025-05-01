package org.appsmith.filmestry.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val success: Boolean?
)