package com.alerecchi.kmm_movies.movie_details.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiMovieDetails(
    val id: Int,
    val overview: String?,
    val title: String,
    val poster_path: String?
)

@Serializable
data class ApiMovieCredits(
    val id: Int,
    val cast: List<ApiCastMember>
)

@Serializable
data class ApiCastMember(
    val id: Int,
    val name: String,
    val profile_path: String?,
    val character: String
)