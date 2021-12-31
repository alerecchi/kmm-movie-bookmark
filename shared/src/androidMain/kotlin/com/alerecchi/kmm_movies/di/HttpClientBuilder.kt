package com.alerecchi.kmm_movies.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual class HttpClientBuilder {
    actual fun build(block: HttpClientConfig<HttpClientEngineConfig>.() -> Unit): HttpClient {
        return HttpClient(OkHttp, block)
    }
}