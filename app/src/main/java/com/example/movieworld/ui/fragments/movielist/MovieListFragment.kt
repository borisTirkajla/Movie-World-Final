package com.example.movieworld.ui.fragments.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movieworld.adapters.MovieListAdapter
import com.example.movieworld.databinding.FragmentMovieListBinding
import com.example.movieworld.util.Constants.DEFAULT_GENRE
import com.example.movieworld.util.Constants.DEFAULT_GENRE_ID
import com.example.movieworld.util.GenresEnum
import com.example.movieworld.util.NetworkListener
import com.example.movieworld.util.NetworkResult
import com.example.movieworld.util.observeOnce
import com.example.movieworld.viewmodels.MainViewModel
import com.example.movieworld.viewmodels.MovieListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MovieListFragment"

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val movieListViewModel: MovieListViewModel by viewModels()

    private var selectedGenre = DEFAULT_GENRE
    private var selectedGenreId = DEFAULT_GENRE_ID

    private lateinit var networkListener: NetworkListener

    private var shouldRefresh: Boolean = false

    private val mAdapter by lazy {
        MovieListAdapter { movie ->
            try {
                val action =
                    MovieListFragmentDirections.actionMovieListFragmentToDetailsFragment(movie.id)
                findNavController().navigate(action)
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }

        }
    }

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val genres = GenresEnum.getValues()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentMovieListBinding.inflate(inflater, container, false).apply {
            _binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        movieListViewModel.readBackOnline.observe(viewLifecycleOwner) {
            movieListViewModel.backOnline = it
        }

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("Network Listener", status.toString())
                    movieListViewModel.networkStatus = status
                    movieListViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        movieListViewModel.readGenreType.asLiveData().observe(viewLifecycleOwner) { value ->
            selectedGenreId = value.selectedGenreId
        }

        binding.floatingActionButton.setOnClickListener {
            if (movieListViewModel.networkStatus) {
                showRadioConfirmationDialog()
            } else {
                movieListViewModel.showNetworkStatus()
            }
        }
    }

    private fun showRadioConfirmationDialog() {
        var index = selectedGenreId
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("List of Genres")
            .setSingleChoiceItems(genres, selectedGenreId) { _, which ->
                index = which
            }
            .setPositiveButton("Ok") { dialog, _ ->
                if (index != selectedGenreId) {
                    setChosenGenre(index, selectedGenre)
                    movieListViewModel.saveGenreType(
                        selectedGenreId = index
                    )
                    shouldRefresh = true
                    requestApiData(GenresEnum.values()[index].genre)
                } else {
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setChosenGenre(selectedGenreId: Int, selectedGenre: GenresEnum) {
        this.selectedGenre = selectedGenre
        this.selectedGenreId = selectedGenreId
    }

    private fun setupRecyclerView() {
        showShimmerEffect()
        binding.recyclerview.apply {
            adapter = mAdapter
        }
    }


    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readMovies.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !shouldRefresh) {
                    shouldRefresh = false
                    val list = database[0].movieList.movies.filterNot {
                        it.image.isEmpty()
                    }
                    mAdapter.setData(list)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData(query: String? = null) {
        Log.d(TAG, "requestApiData")
        mainViewModel.getMovieByGenre(movieListViewModel.applyQueries(query))
        mainViewModel.movieResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        mAdapter.setData(it.movies)
                    }
                    hideShimmerEffect()
                    shouldRefresh = false
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readMovies.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].movieList.movies)
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.visibility = View.INVISIBLE
        binding.recyclerview.visibility = View.VISIBLE
        binding.shimmerFrameLayout.stopShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
