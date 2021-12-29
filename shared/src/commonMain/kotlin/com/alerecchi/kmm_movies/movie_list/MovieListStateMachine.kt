package com.alerecchi.kmm_movies.movie_list

import com.alerecchi.kmm_movies.movie_list.data.TrendingDataSource
import com.alerecchi.kmm_movies.movie_list.di.NetworkModule

class MovieListStateMachine(
    private val trendingDataSource: TrendingDataSource = NetworkModule.provideTrendingDataSource()
) {

    //ADD flow

    suspend fun test() = trendingDataSource.fetchTrending()

}