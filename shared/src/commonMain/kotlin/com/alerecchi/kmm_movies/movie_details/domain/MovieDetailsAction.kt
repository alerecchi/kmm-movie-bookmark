package com.alerecchi.kmm_movies.movie_details.domain

sealed class MovieDetailsAction {

    data class LoadMovie(val id: Int): MovieDetailsAction()
}