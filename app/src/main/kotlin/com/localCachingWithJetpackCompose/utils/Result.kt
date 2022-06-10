package com.localCachingWithJetpackCompose.utils

sealed class Result<T> private constructor(val data: T? = null, val message: String? = null) {
    class Loading<T>(val isLoading: Boolean = true) : Result<T>(null, null)
    class Success<T>(data: T?, message: String? = null) : Result<T>(data, message)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}