package com.alerecchi.kmm_movies.movie_details

import com.alerecchi.kmm_movies.utils.ApiResult
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface MovieDetailsDataSource {
    suspend fun fetchDetails(id: Int): ApiResult<MovieResult>
}

class MovieDetailsDataSourceImpl(
    private val client: HttpClient
): MovieDetailsDataSource {
    override suspend fun fetchDetails(id: Int): ApiResult<MovieResult> {
        return coroutineScope {
            val details: Deferred<MovieDetails> = async(Dispatchers.Default) {
                client.get("/3/movie/$id")
            }

            val credits: Deferred<MovieCredits> = async(Dispatchers.Default) {
                client.get("/3/movie/$id/credits")
            }

            try {
                ApiResult.Success(MovieResult(details.await(), credits.await()))
            } catch (ex: Exception) {
                ApiResult.Error(ex)
            }
        }
    }
}