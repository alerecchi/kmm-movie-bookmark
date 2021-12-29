package com.alerecchi.kmm_movies.movie_list.domain

import com.alerecchi.kmm_movies.movie_list.domain.models.MovieTile

sealed class TrendingMoviesState {

    object Loading: TrendingMoviesState()

    data class ShowMovies(
        val movies: List<MovieTile>
    ): TrendingMoviesState()

    data class NavigateToDetails(
        val id: Int
    ): TrendingMoviesState()

    object Error: TrendingMoviesState()
}