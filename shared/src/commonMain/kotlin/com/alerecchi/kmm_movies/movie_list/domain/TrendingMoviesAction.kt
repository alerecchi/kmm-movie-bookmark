package com.alerecchi.kmm_movies.movie_list.domain

sealed class TrendingMoviesAction {

    object LoadMovies: TrendingMoviesAction()
    data class MovieClicked(val id: Int): TrendingMoviesAction()
}