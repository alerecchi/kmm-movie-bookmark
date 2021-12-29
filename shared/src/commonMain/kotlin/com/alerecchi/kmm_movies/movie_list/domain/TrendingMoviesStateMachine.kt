package com.alerecchi.kmm_movies.movie_list.domain

import com.alerecchi.kmm_movies.di.NetworkModule
import com.alerecchi.kmm_movies.movie_list.data.TrendingDataSource
import com.alerecchi.kmm_movies.utils.ApiResult
import com.alerecchi.kmm_movies.utils.Observer
import com.alerecchi.kmm_movies.utils.StateMachine
import com.alerecchi.kmm_movies.utils.Subject

class TrendingMoviesStateMachine(
    private val trendingDataSource: TrendingDataSource = NetworkModule.provideTrendingDataSource()
) : StateMachine<TrendingMoviesState, TrendingMoviesAction>, Subject<TrendingMoviesState> {

    private val observers = mutableListOf<Observer<TrendingMoviesState>>()
    private lateinit var lastState: TrendingMoviesState

    override fun register(observer: Observer<TrendingMoviesState>) {
        observers.add(observer)
    }

    override fun unRegister(observer: Observer<TrendingMoviesState>) {
        observers.remove(observer)
    }

    override fun updateState(state: TrendingMoviesState) {
        lastState = state
        observers.forEach { it.updateState(state) }
    }

    init {
        updateState(TrendingMoviesState.Loading)
    }

    override suspend fun handleAction(action: TrendingMoviesAction) {
        updateState(reducer(lastState, action))
    }

    override suspend fun reducer(
        state: TrendingMoviesState,
        action: TrendingMoviesAction
    ): TrendingMoviesState {
        return if (action is TrendingMoviesAction.LoadMovies) {
            val response = trendingDataSource.fetchTrending()
            if (response is ApiResult.Success) {
                TrendingMoviesState.ShowMovies(response.result)
            } else {
                TrendingMoviesState.Error
            }
        } else if (state is TrendingMoviesState.ShowMovies && action is TrendingMoviesAction.MovieClicked) {
            TrendingMoviesState.NavigateToDetails(action.id)
        } else {
            state
        }
    }

}