package org.appsmith.filmestry.network.util

enum class NetworkError(val message: String) : Error {
    REQUEST_TIMEOUT("Request timeout"),
    UNAUTHORIZED("Unauthorized"),
    CONFLICT("Conflict"),
    TOO_MANY_REQUESTS("Too many requests"),
    NO_INTERNET("No Internet Connection"),
    PAYLOAD_TOO_LARGE("Payload too large"),
    SERVER_ERROR("Server error"),
    SERIALIZATION("Serialization error"),
    UNKNOWN("Unknown error");
}