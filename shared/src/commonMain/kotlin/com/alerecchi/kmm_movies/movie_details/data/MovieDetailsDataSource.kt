package com.alerecchi.kmm_movies.movie_details.data

import com.alerecchi.kmm_movies.movie_details.data.model.ApiMovieCredits
import com.alerecchi.kmm_movies.movie_details.data.model.ApiMovieDetails
import com.alerecchi.kmm_movies.movie_details.domain.model.CastMember
import com.alerecchi.kmm_movies.movie_details.domain.model.MovieDetails
import com.alerecchi.kmm_movies.utils.ApiResult
import com.alerecchi.kmm_movies.utils.toImageUrl
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface MovieDetailsDataSource {
    suspend fun fetchDetails(id: Int): ApiResult<MovieDetails>
}

class MovieDetailsDataSourceImpl(
    private val client: HttpClient
) : MovieDetailsDataSource {
    override suspend fun fetchDetails(id: Int): ApiResult<MovieDetails> {
        return try {
            withContext(Dispatchers.Default) {
                val details: Deferred<ApiMovieDetails> = async {
                    client.get("/3/movie/$id")
                }

                val credits: Deferred<ApiMovieCredits> = async {
                    client.get("/3/movie/$id/credits")
                }

                val domainDetails = Pair(details.await(), credits.await()).mapToDomain()
                ApiResult.Success(domainDetails)
            }
        } catch (ex: Exception) {
            ApiResult.Error(ex)
        }

    }
}


fun Pair<ApiMovieDetails, ApiMovieCredits>.mapToDomain(): MovieDetails {
    return MovieDetails(
        first.overview,
        first.title,
        first.poster_path.toImageUrl(),
        second.cast.map {
            CastMember(
                it.name,
                it.profile_path.toImageUrl()
            )
        }
    )
}