package com.alerecchi.kmm_movies.movie_details.domain.model

class MovieDetails(
    val overview: String?,
    val title: String,
    val posterPath: String?,
    val cast: List<CastMember>
)

data class CastMember(
    val name: String,
    val profile_path: String?,
)