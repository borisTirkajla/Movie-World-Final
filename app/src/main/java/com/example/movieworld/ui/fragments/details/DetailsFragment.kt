package com.example.movieworld.ui.fragments.details

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.movieworld.R
import com.example.movieworld.adapters.PagerAdapter
import com.example.movieworld.databinding.FragmentDetailsBinding
import com.example.movieworld.models.moviebyid.MovieById
import com.example.movieworld.ui.MainActivity
import com.example.movieworld.ui.fragments.actors.ActorsFragment
import com.example.movieworld.ui.fragments.overview.OverviewFragment
import com.example.movieworld.ui.fragments.similarmovies.SimilarMoviesFragment
import com.example.movieworld.util.Constants
import com.example.movieworld.viewmodels.DetailsViewModel
import com.example.movieworld.viewmodels.FavoriteMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DetailsFragment"

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private val favoriteMoviesViewModel: FavoriteMoviesViewModel by activityViewModels()

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentDetailsBinding.inflate(inflater, container, false).apply {
            _binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.detailsViewModel = detailsViewModel

        (activity as MainActivity).visibilityNavElements(findNavController())

        var savedMovie: MovieById? = null

        try {
            savedMovie = arguments?.getParcelable(Constants.BUNDLE_MOVIE_BY_ID)!!
            detailsViewModel.setMovieResponseFromLocalDatabase(savedMovie)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }

        if (savedMovie == null) {
            arguments?.getString(Constants.BUNDLE_ID)?.let { id ->
                detailsViewModel.movieId = id
                detailsViewModel.findMovieById(id)
            }
        }


        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(ActorsFragment())
        fragments.add(SimilarMoviesFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Actors")
        titles.add("Similar Movies")


        val adapter = PagerAdapter(
            fragments = fragments,
            title = titles,
            childFragmentManager
        )

        binding.viewPager.adapter = adapter
        binding.tablayout.setupWithViewPager(binding.viewPager)

        setToolbar()
    }

    private fun setToolbar() {

        val menu = binding.toolbar.menu
        checkSavedRecipes(menu)

        binding.toolbar.setTitleTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_favorite -> {
                    saveOrRemoveFromFavorites()
                    true
                }
                else -> {
                    true
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }



    private fun checkSavedRecipes(menu: Menu) {
        favoriteMoviesViewModel.readFavoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            try {
                isFavorite = false
                if (favoriteMovies.isEmpty()) {
                    changeMenuItemIconAndColor(
                        menu = menu,
                        isFavorite = isFavorite
                    )
                }
                favoriteMovies.forEach { favoriteMovieEntity ->
                    val isMovieFavorite =
//                        favoriteMovieEntity.id == detailsViewModel.movieResponse.value?.data?.id
                        favoriteMovieEntity.id == detailsViewModel.movieId
                    if (isMovieFavorite) {
                        isFavorite = true
                    }
                    changeMenuItemIconAndColor(
                        menu = menu,
                        isFavorite = isFavorite
                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
    }

    private fun saveOrRemoveFromFavorites() {
        if (isFavorite) {
            removeFromFavorites()
        } else {
            saveToFavorites()
        }
    }

    private fun removeFromFavorites() {
        detailsViewModel.movieResponse.value?.data?.let { movieById ->
            favoriteMoviesViewModel.deleteFavoriteMovie(movieById)
        }
    }

    private fun changeMenuItemIconAndColor(menu: Menu, isFavorite: Boolean) {
        menu.getItem(0).apply {
            if (isFavorite) {
                setIcon(R.drawable.ic_baseline_favorite_24)
                icon?.setTint(ContextCompat.getColor(requireContext(), R.color.red))
            } else {
                setIcon(R.drawable.ic_baseline_favorite_border_24)
                icon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }
    }

    private fun saveToFavorites() {
        detailsViewModel.movieResponse.value?.data?.let { movieById ->
            favoriteMoviesViewModel.insertFavoriteMovie(movieById)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}