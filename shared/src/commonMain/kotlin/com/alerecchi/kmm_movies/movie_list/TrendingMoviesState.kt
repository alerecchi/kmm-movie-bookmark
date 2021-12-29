package com.alerecchi.kmm_movies.movie_list

import com.alerecchi.kmm_movies.movie_list.models.Movie

sealed class TrendingMoviesState {

    object Loading

    data class ShowMovies(
        val movies: List<Movie>
    )
}