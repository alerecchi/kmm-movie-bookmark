package com.alerecchi.kmm_movies.movie_list.data

import com.alerecchi.kmm_movies.movie_list.data.models.TrendingMovies
import com.alerecchi.kmm_movies.movie_list.domain.models.MovieTile
import com.alerecchi.kmm_movies.utils.ApiResult
import com.alerecchi.kmm_movies.utils.getResult
import com.alerecchi.kmm_movies.utils.mapSuccess
import com.alerecchi.kmm_movies.utils.toImageUrl
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface TrendingDataSource {
    suspend fun fetchTrending(): ApiResult<List<MovieTile>>
}

class TrendingDataSourceImpl(
    private val client: HttpClient
) : TrendingDataSource {

    override suspend fun fetchTrending(): ApiResult<List<MovieTile>> {
        return withContext(Dispatchers.Default) {
            client
                .getResult<TrendingMovies>("/3/trending/movie/day")
                .mapSuccess { it.results }
                .mapSuccess { list ->
                    list.map { movie ->
                        MovieTile(
                            movie.id,
                            movie.title,
                            movie.poster_path.toImageUrl()
                        )
                    }
                }
        }
    }
}