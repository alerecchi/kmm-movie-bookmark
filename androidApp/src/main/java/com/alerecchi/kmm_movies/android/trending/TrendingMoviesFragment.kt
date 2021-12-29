package com.alerecchi.kmm_movies.android.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alerecchi.kmm_movies.android.R
import com.alerecchi.kmm_movies.android.details.MovieDetailFragment
import com.alerecchi.kmm_movies.movie_list.MovieListStateMachine
import com.alerecchi.kmm_movies.utils.ApiResult
import kotlinx.coroutines.launch

class TrendingMoviesFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending, container, false)


        val lm = GridLayoutManager(context, 2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.movie_list)
        recyclerView.layoutManager = lm
        lifecycleScope.launch {
            val movies = (MovieListStateMachine().test() as ApiResult.Success).result
            val clickListener: (Int) -> Unit = { id ->
                val fragment = MovieDetailFragment()
                fragment.arguments = Bundle().apply { putInt(MovieDetailFragment.ID_ARG, id) }
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.containerView, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            val adapter = MovieListAdapter(clickListener)
            adapter.submitList(movies)
            recyclerView.adapter = adapter
        }
        return view
    }
}