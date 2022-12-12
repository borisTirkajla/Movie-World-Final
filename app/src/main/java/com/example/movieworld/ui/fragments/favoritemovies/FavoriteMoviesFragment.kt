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

    private val mainViewModel: MainViewModel by viewModels()
    private val favoriteMoviesViewModel: FavoriteMoviesViewModel by activityViewModels()

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy {
        MovieFavoriteListAdapter(requireActivity = requireActivity()) { favoriteMovieEntity ->
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

//        favoriteMoviesViewModel.readFavoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
//            val list = getTransformedList(favoriteMovies)
//            mAdapter.setData(list)
//            hideShimmerEffect()
//        }
    }

//    private fun getTransformedList(favoriteMovies: List<FavoriteMovieEntity>): List<MovieById> {
//        val list = arrayListOf<MovieById>()
//        if (favoriteMovies.isNotEmpty()) {
//            favoriteMovies.forEach { favoriteMovieEntity ->
//                favoriteMovieEntity.movie.apply {
//                    list.add(
//                        MovieById(
//                            actorList = actorList,
//                            awards = awards,
//                            boxOffice = boxOffice,
//                            directorList = directorList,
//                            directors = directors,
//                            errorMessage = errorMessage,
//                            fullTitle = fullTitle,
//                            genreList = genreList,
//                            genres = genres,
//                            id = id,
//                            imDbRating = imDbRating,
////                            imDbRatingVotes = imDbRatingVotes,
//                            image = image,
//                            keywordList = keywordList,
//                            plot = plot,
//                            releaseDate = releaseDate,
//                            runtimeMins = runtimeMins,
//                            runtimeStr = runtimeStr,
//                            similars = similars,
//                            starList = starList,
//                            stars = stars,
//                            title = title,
//                            writerList = writerList,
//                            writers = writers,
//                            year = year
//                        )
//                    )
//                }
//            }
//        } else {
//            Toast.makeText(
//                requireContext(),
//                "Please add some favorite movies.",
//                Toast.LENGTH_SHORT
//            ).show()
//            hideShimmerEffect()
//        }
//        return list
//    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
//        showShimmerEffect()
        recyclerView.apply {
            adapter = mAdapter
        }
    }

//    private fun showShimmerEffect() {
//        binding.shimmerFrameLayout.visibility = View.VISIBLE
//        binding.shimmerFrameLayout.startShimmer()
//        binding.recyclerview.visibility = View.GONE
//    }
//
//    private fun hideShimmerEffect() {
//        binding.shimmerFrameLayout.visibility = View.INVISIBLE
//        binding.recyclerview.visibility = View.VISIBLE
//        binding.shimmerFrameLayout.stopShimmer()
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}