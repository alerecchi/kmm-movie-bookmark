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
import com.alerecchi.kmm_movies.movie_details.domain.model.CastMember

class CastAdapter : ListAdapter<CastMember, CastViewHolder>(CastDiffCallBack) {

    object CastDiffCallBack : DiffUtil.ItemCallback<CastMember>() {
        override fun areItemsTheSame(oldItem: CastMember, newItem: CastMember): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
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

class CastViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(member: CastMember) {
        itemView.findViewById<TextView>(R.id.name).text = member.name
        itemView.findViewById<ImageView>(R.id.profile).load(member.profile_path)
    }
}