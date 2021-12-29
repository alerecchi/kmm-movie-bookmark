package com.alerecchi.kmm_movies.movie_details

import com.alerecchi.kmm_movies.di.NetworkModule
import com.alerecchi.kmm_movies.utils.ApiResult

class MovieDetailsStateMachine(
    val id: Int,
    private val dataSource: MovieDetailsDataSource = NetworkModule.provideMovieDetailsDataSource()
) {

    suspend fun test(): MovieResult = (dataSource.fetchDetails(id) as ApiResult.Success).result
}