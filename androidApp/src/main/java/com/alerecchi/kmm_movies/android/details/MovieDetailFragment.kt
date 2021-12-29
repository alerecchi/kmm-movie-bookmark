package com.alerecchi.kmm_movies.android.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alerecchi.kmm_movies.android.R
import com.alerecchi.kmm_movies.movie_details.MovieDetailsStateMachine
import java.net.URL

class MovieDetailFragment: Fragment() {

    companion object {
        const val ID_ARG = "id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val id = requireArguments().getInt(ID_ARG)!!
        lifecycleScope.launchWhenStarted {
            val state = MovieDetailsStateMachine(id).test()

            view.findViewById<TextView>(R.id.title).text = state.details.title
            view.findViewById<TextView>(R.id.description).text = state.details.overview
            val imageUrl = URL("https", "image.tmdb.org/t/p/original/", state.details.poster_path)
            view.findViewById<ImageView>(R.id.poster).load(imageUrl.toString())
            val adapter = CastAdapter()
            val lm = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            view.findViewById<RecyclerView>(R.id.cast).apply {
                adapter.submitList(state.cast.cast)
                this.adapter = adapter
                this.layoutManager = lm
            }

        }


        return view
    }
}