package org.appsmith.flickcase.model.authentication

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val success: Boolean?
)