package org.appsmith.filmestry.model.authentication

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val success: Boolean?
)