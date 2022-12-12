package com.example.movieworld.ui.fragments.moviebysearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieworld.adapters.MovieListAdapter
import com.example.movieworld.databinding.FragmentSearchMovieBinding
import com.example.movieworld.models.moviebygenre.Movie
import com.example.movieworld.util.NetworkResult
import com.example.movieworld.viewmodels.SearchMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SearchMovieFragment"

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    private val searchMovieViewModel: SearchMovieViewModel by viewModels()

    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy {
        MovieListAdapter { movie ->
            try {
                val action =
                    SearchMovieFragmentDirections.actionSearchMovieFragment2ToDetailsFragment(movie.id)
                findNavController().navigate(action)
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        FragmentSearchMovieBinding.inflate(inflater, container, false).apply {
            _binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int
            ) {
                if (s.isEmpty()) {
                    mAdapter.setData(listOf())
                    binding.textViewResults.isVisible = false
                }
            }
        })

        binding.textFieldSearch.setEndIconOnClickListener {
            binding.editTextSearch.text.let {
                if (it != null && it.length >= 2) {
                    searchApiData(it.toString())
                }
            }
        }
        binding.editTextSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH

        binding.editTextSearch.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.let {
                    binding.editTextSearch.text.let {
                        if (it != null && it.length >= 2) {
                            searchApiData(it.toString())
                        }
                    }
                    false
                }
            }
            true
        }
    }


    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        searchMovieViewModel.searchMovie(searchMovieViewModel.applySearchQuery(searchQuery))
        searchMovieViewModel.searchedMovieResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val movie = response.data
                    val movieList: List<Movie> = movie!!.let { movieBySearch ->
                        movieBySearch.results.map {
                            Movie(
                                title = it.title,
                                description = it.description,
                                id = it.id,
                                image = it.image
                            )
                        }
                    }

                    val list = movieList.filterNot {
                        it.image.isEmpty()
                    }
                    mAdapter.setData(list)
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(), "response.message.toString()", Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        hideShimmerEffect()
        binding.textViewResults.isVisible = false
        binding.recyclerView.apply {
            adapter = mAdapter
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.textViewResults.isVisible = false
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.textViewResults.isVisible = true
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}