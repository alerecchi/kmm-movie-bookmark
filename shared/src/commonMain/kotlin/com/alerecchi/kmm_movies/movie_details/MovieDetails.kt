package com.alerecchi.kmm_movies.movie_details

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
    val id: Int,
    val overview: String?,
    val title: String,
    val poster_path: String?
)

@Serializable
data class MovieCredits(
    val id: Int,
    val cast: List<CastMember>
)

@Serializable
data class CastMember(
    val id: Int,
    val name: String,
    val profile_path: String?,
    val character: String
)

data class MovieResult(
    val details: MovieDetails,
    val cast: MovieCredits
)