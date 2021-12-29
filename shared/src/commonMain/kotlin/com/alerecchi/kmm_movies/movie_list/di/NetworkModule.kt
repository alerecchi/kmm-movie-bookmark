package com.alerecchi.kmm_movies.movie_list.di

import com.alerecchi.kmm_movies.BuildKonfig
import com.alerecchi.kmm_movies.movie_list.data.TrendingDataSource
import com.alerecchi.kmm_movies.movie_list.data.TrendingDataSourceImpl
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

object NetworkModule {

    private val httpClient: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging)

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
            }
            host = "api.themoviedb.org"
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(BuildKonfig.API_KEY, BuildKonfig.API_KEY)
                }
            }
        }
    }

    fun provideTrendingDataSource(): TrendingDataSource {
        return TrendingDataSourceImpl(httpClient)
    }
}