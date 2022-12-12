package com.example.movieworld.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.example.movieworld.R
import com.example.movieworld.databinding.MovieRowLayoutBinding
import com.example.movieworld.models.moviebygenre.Movie
import com.example.movieworld.ui.MainActivity
import com.example.movieworld.util.MovieDiffUtil

class MovieListAdapter(private val onItemClicked: (movie: Movie) -> Unit) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private var movieList = emptyList<Movie>()

    class ViewHolder(private val binding: MovieRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {

            val imageLoader = binding.imageViewPhoto.context.imageLoader
            val request = ImageRequest.Builder(binding.imageViewPhoto.context)
                .data(movie.image)
                .listener(
                    onStart = {
                        binding.progressBar.isVisible = true},
                    onSuccess = {_,_, ->
                        binding.progressBar.isVisible = false
                    }
                )
                .crossfade(600)
                .size(200, 300)
//                .memoryCachePolicy(CachePolicy.DISABLED)
//                .diskCachePolicy(CachePolicy.DISABLED)
                .target(binding.imageViewPhoto)
                .build()
            imageLoader.enqueue(request)

            binding.movie = movie
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.itemView.setOnClickListener {
            onItemClicked(currentMovie)
        }
        holder.bind(currentMovie)
    }

    override fun getItemCount() = movieList.size

    fun setData(newData: List<Movie>) {
        val movieDiffUtil = MovieDiffUtil(movieList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtil)
        movieList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}