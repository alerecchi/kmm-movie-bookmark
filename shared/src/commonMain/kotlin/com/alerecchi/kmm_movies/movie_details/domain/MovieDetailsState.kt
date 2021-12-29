package com.alerecchi.kmm_movies.movie_details.domain

import com.alerecchi.kmm_movies.movie_details.domain.model.MovieDetails

sealed class MovieDetailsState {

    object Loading: MovieDetailsState()

    data class ShowDetails(val content: MovieDetails): MovieDetailsState()

    object ShowError: MovieDetailsState()
}