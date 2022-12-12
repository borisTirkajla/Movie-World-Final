package com.example.movieworld.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.example.movieworld.R
import com.example.movieworld.databinding.SimilarMovieRowLayoutBinding
import com.example.movieworld.models.moviebyid.Similar
import com.example.movieworld.util.MovieDiffUtil

class SimilarMoviesAdapter(private val onItemClicked: (id: String) -> Unit) :
    RecyclerView.Adapter<SimilarMoviesAdapter.ViewHolder>() {

    private var movieList = emptyList<Similar>()

    class ViewHolder(private val binding: SimilarMovieRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(similarMovie: Similar) {

            val imageLoader = binding.imageViewPhoto.context.imageLoader
            val request = ImageRequest.Builder(binding.imageViewPhoto.context)
                .data(similarMovie.image)
                .crossfade(600)
                .size(200, 300)
                .listener(
                    onStart = {
                        binding.progressBar.isVisible = true
                    },
                    onSuccess = { _, _ ->
                        binding.progressBar.isVisible = false
                    }
                )
                .target(binding.imageViewPhoto)
                .build()
            imageLoader.enqueue(request)
            binding.similar = similarMovie
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SimilarMovieRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val similarMovie = movieList[position]
        holder.itemView.setOnClickListener {
            onItemClicked(similarMovie.id)
        }
        holder.bind(similarMovie)
    }

    override fun getItemCount() = movieList.size

    fun setData(newData: List<Similar>) {
        val movieDiffUtil = MovieDiffUtil(movieList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtil)
        movieList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}