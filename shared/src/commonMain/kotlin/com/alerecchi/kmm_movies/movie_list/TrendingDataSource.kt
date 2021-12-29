package com.alerecchi.kmm_movies.movie_list

import com.alerecchi.kmm_movies.movie_list.models.Movie
import com.alerecchi.kmm_movies.movie_list.models.TrendingMovies
import com.alerecchi.kmm_movies.utils.ApiResult
import com.alerecchi.kmm_movies.utils.getResult
import com.alerecchi.kmm_movies.utils.mapSuccess
import io.ktor.client.*

interface TrendingDataSource {
    suspend fun fetchTrending(): ApiResult<List<Movie>>
}

class TrendingDataSourceImpl(
    private val client: HttpClient
) : TrendingDataSource {

    override suspend fun fetchTrending(): ApiResult<List<Movie>> {
        return client
            .getResult<TrendingMovies>("/3/trending/movie/day")
            .mapSuccess { it.results }
    }
}