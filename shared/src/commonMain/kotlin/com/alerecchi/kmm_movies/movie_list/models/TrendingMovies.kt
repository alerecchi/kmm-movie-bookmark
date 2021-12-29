package com.alerecchi.kmm_movies.movie_list.models

import kotlinx.serialization.Serializable

@Serializable
data class TrendingMovies (
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class Movie(
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val popularity: Double,
    val vote_average: Double,
    val video: Boolean,
    val vote_count: Int,
    val media_type: String
)