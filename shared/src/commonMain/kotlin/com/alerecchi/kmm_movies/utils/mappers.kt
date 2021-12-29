package com.alerecchi.kmm_movies.utils

import java.net.URL

fun String?.toImageUrl(): String {
    return if (this != null) {
        URL(
            "https",
            "image.tmdb.org/t/p/original/",
            this
        ).toString()
    } else {
        ""
    }
}