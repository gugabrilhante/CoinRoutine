package com.gustavo.brilhante.coinroutine.core.domain

sealed interface Result<out D, out E : Error> {
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : com.gustavo.brilhante.coinroutine.core.domain.Error>(val error: E) : Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(map(data))
    is Result.Error -> Result.Error(error)
}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> = map {}

inline fun <T, E : Error> Result<T, E>.asSuccess(action: (T) -> Unit): Result<T, E> = when (this) {
    is Result.Success -> {
        action(data)
        this
    }

    is Result.Error -> this
}

typealias EmptyResult<E> = Result<Unit, E>