package com.example.movieworld.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.example.movieworld.R
import com.example.movieworld.databinding.ActorRowLayoutBinding
import com.example.movieworld.models.moviebyid.Actor
import com.example.movieworld.util.MovieDiffUtil

class ActorsListAdapter(private val onItemClicked: (id: String) -> Unit) :
    RecyclerView.Adapter<ActorsListAdapter.ViewHolder>() {

    private var actorList = emptyList<Actor>()

    class ViewHolder(private val binding: ActorRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(actor: Actor) {

            val imageLoader = binding.imageViewPhoto.context.imageLoader
            val request = ImageRequest.Builder(binding.imageViewPhoto.context)
                .data(actor.image)
                .crossfade(600)
                .size(200, 300)
                .placeholder(R.drawable.loading_animation)
//                .memoryCachePolicy(CachePolicy.DISABLED)
//                .diskCachePolicy(CachePolicy.DISABLED)
                .target(binding.imageViewPhoto)
                .build()
            imageLoader.enqueue(request)

            binding.actor = actor
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ActorRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMovie = actorList[position]
        holder.itemView.setOnClickListener {
            onItemClicked(currentMovie.id)
        }
        holder.bind(currentMovie)
    }

    override fun getItemCount() = actorList.size

    fun setData(newData: List<Actor>) {
        val movieDiffUtil = MovieDiffUtil(actorList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtil)
        actorList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}