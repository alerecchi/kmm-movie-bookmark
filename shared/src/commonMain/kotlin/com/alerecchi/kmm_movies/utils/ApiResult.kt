package com.alerecchi.kmm_movies.utils

import io.ktor.client.*
import io.ktor.client.request.*

sealed class ApiResult<T> {
    data class Success<T>(
        val result: T
    ) : ApiResult<T>()

    data class Error<T>(
        val throwable: Throwable
    ) : ApiResult<T>()
}

suspend inline fun <reified T> HttpClient.getResult(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): ApiResult<T> {
    return try {
        ApiResult.Success(
            this.get(url)
        )
    } catch (e: Exception) {
        ApiResult.Error(e)
    }
}

inline fun <T, R> ApiResult<T>.mapSuccess(mapper: (T) -> R): ApiResult<R> {
    return if (this is ApiResult.Success) {
        val mappedResult = mapper(result)
        ApiResult.Success(mappedResult)
    } else {
        @Suppress("UNCHECKED_CAST")
        this as ApiResult<R>
    }
}