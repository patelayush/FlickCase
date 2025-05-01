package org.appsmith.filmestry.network.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse


sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : org.appsmith.filmestry.network.util.Error>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }

        is Result.Success -> this
    }
}

// util function
suspend inline fun <reified T> result(response: HttpResponse) =
    when (response.status.value) {
        in 200..299 -> Result.Success(response.body() as T)
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        401 -> Result.Error(NetworkError.UNAUTHORIZED)
        409 -> Result.Error(NetworkError.CONFLICT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        500 -> Result.Error(NetworkError.SERVER_ERROR)
        in 400..499 -> Result.Error(NetworkError.UNKNOWN)
        else -> Result.Error(NetworkError.UNKNOWN)
    }

typealias EmptyResult<E> = Result<Unit, E>