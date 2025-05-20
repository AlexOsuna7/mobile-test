package com.example.mobile_test.core.common

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val message: String, val throwable: Throwable? = null): Result<Nothing>()

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(message: String, throwable: Throwable? = null): Result<Nothing> =
            Error(message, throwable)
    }

    fun isSuccess(): Boolean = this is Success<T>
    fun isError(): Boolean = this is Error
}
