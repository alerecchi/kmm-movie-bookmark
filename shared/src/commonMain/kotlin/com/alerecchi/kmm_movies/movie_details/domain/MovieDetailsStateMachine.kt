package com.alerecchi.kmm_movies.movie_details.domain

import com.alerecchi.kmm_movies.di.NetworkModule
import com.alerecchi.kmm_movies.movie_details.data.MovieDetailsDataSource
import com.alerecchi.kmm_movies.utils.ApiResult
import com.alerecchi.kmm_movies.utils.Observer
import com.alerecchi.kmm_movies.utils.StateMachine
import com.alerecchi.kmm_movies.utils.Subject

class MovieDetailsStateMachine(
    private val dataSource: MovieDetailsDataSource = NetworkModule.provideMovieDetailsDataSource()
): StateMachine<MovieDetailsState, MovieDetailsAction>,Subject<MovieDetailsState>  {

    private val observers: MutableList<Observer<MovieDetailsState>> = mutableListOf()

    override fun unRegister(observer: Observer<MovieDetailsState>) {
        observers.remove(observer)
    }

    override fun register(observer: Observer<MovieDetailsState>) {
        observers.add(observer)
    }

    override fun updateState(state: MovieDetailsState) {
        lastState = state
        observers.forEach { it.updateState(state) }
    }

    private lateinit var lastState: MovieDetailsState

    init {
        updateState(MovieDetailsState.Loading)
    }

/*    suspend fun fetchDetails(id: Int) {
        val result = dataSource.fetchDetails(id)
        if(result is ApiResult.Success) {
            updateState(MovieDetailsState.ShowDetails(result.result))
        } else {
            updateState(MovieDetailsState.ShowError)
        }
    }*/

    override suspend fun handleAction(action: MovieDetailsAction) {
        updateState(reducer(lastState, action))
    }

    override suspend fun reducer(state: MovieDetailsState, action: MovieDetailsAction): MovieDetailsState {
        return if(state is MovieDetailsState.Loading && action is MovieDetailsAction.LoadMovie) {
            val response = dataSource.fetchDetails(action.id)
            if(response is ApiResult.Success) {
                MovieDetailsState.ShowDetails(response.result)
            } else {
                MovieDetailsState.ShowError
            }
        } else {
            state
        }
    }


}

