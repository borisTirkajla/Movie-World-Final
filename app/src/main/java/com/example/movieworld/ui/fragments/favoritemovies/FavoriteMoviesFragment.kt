package com.example.movieworld.ui.fragments.favoritemovies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movieworld.adapters.MovieFavoriteListAdapter
import com.example.movieworld.data.database.entities.FavoriteMovieEntity
import com.example.movieworld.databinding.FragmentFavoriteMoviesBinding
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.util.observeOnce
import com.example.movieworld.viewmodels.FavoriteMoviesViewModel
import com.example.movieworld.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "FavoriteMoviesFragment"

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment() {

    private val favoriteMoviesViewModel: FavoriteMoviesViewModel by activityViewModels()

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy {
        MovieFavoriteListAdapter(
            requireActivity = requireActivity(),
            favoriteMoviesViewModel = favoriteMoviesViewModel
        ) { favoriteMovieEntity ->
            try {
                val action =
                    FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToDetailsFragment(
                        movieById = favoriteMovieEntity.movie,
                        id = favoriteMovieEntity.id
                    )
                findNavController().navigate(action)
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentFavoriteMoviesBinding.inflate(inflater, container, false).apply {
            _binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.favoriteMoviesViewModel = favoriteMoviesViewModel
        binding.mAdapter = mAdapter
        setupRecyclerView(binding.recyclerview)

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.clearContextualActionMode()
        _binding = null
    }
}