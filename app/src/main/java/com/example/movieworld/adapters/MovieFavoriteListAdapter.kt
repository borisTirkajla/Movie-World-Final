package com.example.movieworld.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.example.movieworld.R
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.databinding.MovieRowLayoutBinding
import com.example.movieworld.models.moviebygenre.Movie
import com.example.movieworld.util.MovieDiffUtil

class MovieFavoriteListAdapter(
    private val requireActivity: FragmentActivity,
    private val onItemClicked: (favMovieEntity: FavoriteMovieEntity) -> Unit
) :
    RecyclerView.Adapter<MovieFavoriteListAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private var selectedMovies = arrayListOf<FavoriteMovieEntity>()
    private var movieList = emptyList<FavoriteMovieEntity>()

    class ViewHolder(val binding: MovieRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favMovieEntity: FavoriteMovieEntity) {

            val imageLoader = binding.imageViewPhoto.context.imageLoader
            val request = ImageRequest.Builder(binding.imageViewPhoto.context)
                .data(favMovieEntity.movie.image)
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
//                .placeholder(R.drawable.loading_animation)
//                .memoryCachePolicy(CachePolicy.DISABLED)
//                .diskCachePolicy(CachePolicy.DISABLED)
                .target(binding.imageViewPhoto)
                .build()
            imageLoader.enqueue(request)

            binding.movie =
                Movie(
                    id = favMovieEntity.id,
                    image = favMovieEntity.movie.image,
                    plot = favMovieEntity.movie.plot,
                    title = favMovieEntity.movie.title ?: "Error: No Title Found."
                )
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
//        holder.itemView.setOnClickListener {
        holder.binding.cardViewRow.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentMovie)
            } else {
                onItemClicked(currentMovie)
            }
        }

        /**
         * Long Click Listener
         * */
        holder.itemView.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder,currentMovie)
                true
            }else{
                multiSelection = false
                false
            }
        }
        holder.bind(currentMovie)
    }

    private fun applySelection(holder: ViewHolder, currentMovie: FavoriteMovieEntity) {
        if (selectedMovies.contains(currentMovie)) {
            selectedMovies.remove(currentMovie)
            changeMovieStyle(holder, R.color.lightGray, R.color.lightMediumGray)
        } else {
            selectedMovies.add(currentMovie)
            changeMovieStyle(holder, R.color.cardBackgroundLightColor, R.color.white)
        }
    }

    private fun changeMovieStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
//        holder.binding.constraintLayoutDetails.setBackgroundColor(
//            ContextCompat.getColor(requireActivity, backgroundColor)
//        )
//        holder.binding.cardViewRow.strokeColor = R.color.white
        holder.binding.cardViewRow.strokeWidth = 10
    }

    override fun getItemCount() = movieList.size

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        applyStatusBarColor(R.color.contextualActionBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        multiSelection = false
        selectedMovies.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newData: List<FavoriteMovieEntity>) {
        val movieDiffUtil = MovieDiffUtil(movieList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(movieDiffUtil)
        movieList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}