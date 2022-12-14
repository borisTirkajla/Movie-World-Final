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
import com.example.movieworld.util.TypeConvertor
import com.example.movieworld.viewmodels.FavoriteMoviesViewModel

class MovieFavoriteListAdapter(
    private val requireActivity: FragmentActivity,
    private val favoriteMoviesViewModel: FavoriteMoviesViewModel,
    private val onItemClicked: (favMovieEntity: FavoriteMovieEntity) -> Unit
) :
    RecyclerView.Adapter<MovieFavoriteListAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode
    private var selectedMovies = arrayListOf<FavoriteMovieEntity>()
    private var myViewHolder = arrayListOf<ViewHolder>()
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
                .target(binding.imageViewPhoto)
                .build()
            imageLoader.enqueue(request)

            val movie: Movie = TypeConvertor.favoriteMovieEntityToMovie(favMovieEntity)
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
        myViewHolder.add(holder)
        val currentMovie = movieList[position]

        /**ClickListener*/
        holder.binding.cardViewRow.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentMovie)
            } else {
                onItemClicked(currentMovie)
            }
        }

        /**Long ClickListener*/
        holder.binding.cardViewRow.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentMovie)
                true
            } else {
                multiSelection = false
                false
            }
        }
        holder.bind(currentMovie)
    }

    private fun applySelection(holder: ViewHolder, currentMovie: FavoriteMovieEntity) {
        if (selectedMovies.contains(currentMovie)) {
            selectedMovies.remove(currentMovie)
            changeMovieStyle(
                holder = holder,
                isVisible = false,
                strokeWidth = 2
            )
            applyActionModeTitle()
        } else {
            selectedMovies.add(currentMovie)
            changeMovieStyle(
                holder = holder,
                isVisible = true,
                strokeWidth = 4
            )
            applyActionModeTitle()
        }
    }

    private fun applyActionModeTitle() {
        when (selectedMovies.size) {
            0 -> mActionMode.finish()
            1 -> mActionMode.title = "${selectedMovies.size} item selected"
            else -> mActionMode.title = "${selectedMovies.size} items selected"
        }
    }

    private fun changeMovieStyle(holder: ViewHolder, isVisible: Boolean, strokeWidth: Int) {
        holder.binding.viewSelectedItem.isVisible = isVisible
        holder.binding.cardViewRow.strokeWidth = strokeWidth
    }

    override fun getItemCount() = movieList.size

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        if (actionMode != null) {
            mActionMode = actionMode
        }
        applyStatusBarColor(R.color.statusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.action_delete_favorite_movie) {
            selectedMovies.forEach { movieEntity ->
                val movieById = TypeConvertor.favoriteMovieEntityToMovieById(movieEntity)
                favoriteMoviesViewModel.deleteFavoriteMovie(
                    movieById = movieById
                )
            }
            multiSelection = false
            selectedMovies.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolder.forEach { holder ->
            changeMovieStyle(
                holder = holder,
                isVisible = false,
                strokeWidth = 2
            )
        }
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

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}