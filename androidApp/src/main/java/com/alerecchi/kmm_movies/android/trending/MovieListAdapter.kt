package com.alerecchi.kmm_movies.android.trending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alerecchi.kmm_movies.android.R
import com.alerecchi.kmm_movies.movie_list.models.Movie
import java.net.URL

class MovieListAdapter(private val clickListener: (Int) -> Unit) :
    ListAdapter<Movie, MovieTileViewHolder>(MovieDiffCallBack) {

    object MovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTileViewHolder {
        return MovieTileViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_movie_tile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieTileViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class MovieTileViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    fun bind(movie: Movie, clickListener: (Int) -> Unit) {
        itemView.findViewById<TextView>(R.id.title).text = movie.title
        val imageUrl = URL("https", "image.tmdb.org/t/p/original/", movie.poster_path)
        itemView.findViewById<ImageView>(R.id.poster).load(imageUrl.toString())
        itemView.setOnClickListener {
            clickListener(movie.id)
        }
    }//TODO get url for images
}