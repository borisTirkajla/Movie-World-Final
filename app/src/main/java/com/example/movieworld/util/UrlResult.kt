package com.example.movieworld.util

sealed class UrlResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class HasUrl<T>(data: T) : UrlResult<T>(data)
    class NoUrl<T>(message: String?) : UrlResult<T>(message = message)
    class Loading<T>() : UrlResult<T>()
}