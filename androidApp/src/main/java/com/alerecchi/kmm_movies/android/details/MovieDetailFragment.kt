package com.alerecchi.kmm_movies.android.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alerecchi.kmm_movies.android.R
import com.alerecchi.kmm_movies.movie_details.domain.MovieDetailsAction
import com.alerecchi.kmm_movies.movie_details.domain.MovieDetailsState
import com.alerecchi.kmm_movies.movie_details.domain.MovieDetailsStateMachine
import com.alerecchi.kmm_movies.utils.Observer

class MovieDetailFragment: Fragment(), Observer<MovieDetailsState> {

    companion object {
        const val ID_ARG = "id"
    }

    private val stateMachine = MovieDetailsStateMachine()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stateMachine.register(this)
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val id = requireArguments().getInt(ID_ARG)
        lifecycleScope.launchWhenStarted {
            stateMachine.handleAction(MovieDetailsAction.LoadMovie(id))
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stateMachine.unRegister(this)
    }

    override fun updateState(state: MovieDetailsState) {
        when(state) {
            MovieDetailsState.Loading -> {
                //do nothing
            }
            is MovieDetailsState.ShowDetails -> {
                view?.run {
                    findViewById<TextView>(R.id.title)?.text = state.content.title
                    findViewById<TextView>(R.id.description).text = state.content.overview
                    findViewById<ImageView>(R.id.poster).load(state.content.posterPath)
                    val adapter = CastAdapter()
                    val lm = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    findViewById<RecyclerView>(R.id.cast).let { rec ->
                        adapter.submitList(state.content.cast)
                        rec.adapter = adapter
                        rec.layoutManager = lm
                    }
                }

            }
            MovieDetailsState.ShowError -> {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }

}