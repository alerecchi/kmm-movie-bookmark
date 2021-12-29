package com.alerecchi.kmm_movies.android.details

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
import com.alerecchi.kmm_movies.movie_details.CastMember
import com.alerecchi.kmm_movies.movie_list.models.Movie
import java.net.URL

class CastAdapter: ListAdapter<CastMember, CastViewHolder>(CastDiffCallBack) {

    object CastDiffCallBack : DiffUtil.ItemCallback<CastMember>() {
        override fun areItemsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_cast_tile,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

class CastViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bindTo(member: CastMember) {
        itemView.findViewById<TextView>(R.id.name).text = member.name

        val imageUrl = URL("https", "image.tmdb.org/t/p/original/", member.profile_path ?: "")
        itemView.findViewById<ImageView>(R.id.profile).load(imageUrl.toString())
    }
}