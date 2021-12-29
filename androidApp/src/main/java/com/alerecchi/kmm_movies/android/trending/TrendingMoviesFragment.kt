package com.alerecchi.kmm_movies.android.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alerecchi.kmm_movies.android.R
import com.alerecchi.kmm_movies.android.details.MovieDetailFragment
import com.alerecchi.kmm_movies.movie_list.domain.TrendingMoviesAction
import com.alerecchi.kmm_movies.movie_list.domain.TrendingMoviesState
import com.alerecchi.kmm_movies.movie_list.domain.TrendingMoviesStateMachine
import com.alerecchi.kmm_movies.utils.Observer

class TrendingMoviesFragment: Fragment(), Observer<TrendingMoviesState> {

    private val stateMachine = TrendingMoviesStateMachine()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stateMachine.register(this)
        val view = inflater.inflate(R.layout.fragment_trending, container, false)
        val lm = GridLayoutManager(context, 2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.movie_list)
        recyclerView.layoutManager = lm
        lifecycleScope.launchWhenStarted {
            stateMachine.handleAction(TrendingMoviesAction.LoadMovies)
        }
        return view
    }

    override fun onDestroyView() {
        stateMachine.unRegister(this)
        super.onDestroyView()
    }

    override fun updateState(state: TrendingMoviesState) {
        when(state) {
            TrendingMoviesState.Error -> {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
            TrendingMoviesState.Loading -> {
                //do nothing
            }
            is TrendingMoviesState.NavigateToDetails -> {
                val fragment = MovieDetailFragment()
                fragment.arguments = Bundle().apply { putInt(MovieDetailFragment.ID_ARG, state.id) }
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.containerView, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            is TrendingMoviesState.ShowMovies -> {
                val clickListener: (Int) -> Unit = { id ->
                    lifecycleScope.launchWhenStarted {
                        stateMachine.handleAction(TrendingMoviesAction.MovieClicked(id))
                    }
                }
                val adapter = MovieListAdapter(clickListener)
                adapter.submitList(state.movies)
                view?.findViewById<RecyclerView>(R.id.movie_list)?.adapter = adapter
            }
        }

    }
}