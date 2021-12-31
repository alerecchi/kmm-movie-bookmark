package com.alerecchi.kmm_movies.utils

import io.ktor.http.*

fun String?.toImageUrl(): String {
    return if (this != null) {
        URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "image.tmdb.org/t/p/original/",
            encodedPath = this
        ).buildString()
    } else {
        ""
    }
}