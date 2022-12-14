package com.example.movieworld.ui.fragments.overview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.movieworld.databinding.FragmentOverviewBinding
import com.example.movieworld.models.moviebyid.BoxOffice
import com.example.movieworld.models.moviebyid.Director
import com.example.movieworld.models.moviebyid.Genre
import com.example.movieworld.models.moviebyid.Writer
import com.example.movieworld.ui.PlayerActivity
import com.example.movieworld.util.Constants
import com.example.movieworld.util.NetworkResult
import com.example.movieworld.util.UrlResult
import com.example.movieworld.viewmodels.DetailsViewModel
import com.example.movieworld.viewmodels.TrailerViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private val trailerViewModel: TrailerViewModel by viewModels()

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentOverviewBinding.inflate(inflater, container, false).apply {
            _binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.detailsViewModel = detailsViewModel

        detailsViewModel.movieResponse.observe(viewLifecycleOwner) {
            if (it.data?.id != null) {
                detailsViewModel.readMovieTrailerUrl()
            }
        }

        binding.extendedFabTrailer.setOnClickListener {
            trailerViewModel.shouldPlayTrailer = true
            when (detailsViewModel.trailerUrl) {
                is UrlResult.NoUrl -> {
                    detailsViewModel.movieResponse.value?.data?.id?.let {
                        trailerViewModel.getTrailerById(it)
                    }
                }
                is UrlResult.Loading -> {
                    Toast.makeText(requireContext(), "Please try again later", Toast.LENGTH_LONG)
                        .show()
                }
                is UrlResult.HasUrl -> {
                    detailsViewModel.trailerUrl.data?.let {
                        trailerViewModel.setTrailerById(it)
                    }
                }
            }
        }


        trailerViewModel.trailerUrl.observe(viewLifecycleOwner) { trailerUrl ->
            if (trailerUrl.isNullOrEmpty() && trailerViewModel.shouldPlayTrailer) {
                Snackbar.make(
                    binding.root,
                    "There is no trailer for this movie.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (trailerViewModel.shouldPlayTrailer) {
                val i = Intent(requireContext(), PlayerActivity::class.java)
                i.putExtra(
                    Constants.BUNDLE_TRAILER_URL,
                    trailerUrl
                )
                i.putExtra(
                    Constants.BUNDLE_TITLE,
                    detailsViewModel.movieResponse.value?.data?.title
                )
                requireContext().startActivity(i)
            }
            trailerViewModel.shouldPlayTrailer = false
        }
        setupLayouts()
    }

    private fun setupLayouts() {
        detailsViewModel.movieResponse.observe(viewLifecycleOwner) { networkResult ->
            when (networkResult) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    networkResult.data?.let { movieById ->
                        binding.movieById = movieById
                        movieById.genreList?.let {
                            setupChips(it)
                        }
                        setupDirectorsLayoutVisibility(movieById.directorList)
                        setupWritersLayoutVisibility(movieById.writerList)
                        setupReleaseDateLayoutVisibility(movieById.releaseDate)
                        setupAwardsLayoutVisibility(movieById.awards)
                        setupBudgetLayoutVisibility(movieById.boxOffice)
                        setupRuntimeLayoutVisibility(movieById.runtimeMins)
                        setupKeywordsLayoutVisibility(movieById.keywordList)
                    }
                }
            }
        }
    }

    private fun setupKeywordsLayoutVisibility(keywordList: List<String>?) {
        if (!keywordList.isNullOrEmpty()) {
            binding.linearLayoutKeywords.visibility = View.VISIBLE
        }
    }

    private fun setupRuntimeLayoutVisibility(runtimeMins: String?) {
        if (!runtimeMins.isNullOrEmpty()) {
            binding.linearLayoutRuntime.visibility = View.VISIBLE
        }

    }

    private fun setupBudgetLayoutVisibility(boxOffice: BoxOffice?) {
        if (boxOffice != null) {
            if (!boxOffice.budget.isNullOrEmpty()) {
                binding.linearLayoutBudget.visibility = View.VISIBLE
            }
        }
    }


    private fun setupAwardsLayoutVisibility(awards: String?) {
        if (!awards.isNullOrEmpty()) {
            binding.linearLayoutAwards.visibility = View.VISIBLE
        }
    }

    private fun setupReleaseDateLayoutVisibility(releaseDate: String?) {
        if (!releaseDate.isNullOrEmpty()) {
            binding.linearLayoutReleaseDate.visibility = View.VISIBLE
        }
    }

    private fun setupWritersLayoutVisibility(writerList: List<Writer>?) {
        if (!writerList.isNullOrEmpty()) {
            binding.linearLayoutWriters.visibility = View.VISIBLE
        }
    }

    private fun setupDirectorsLayoutVisibility(directorList: List<Director>?) {
        if (!directorList.isNullOrEmpty()) {
            binding.linearLayoutDirectors.visibility = View.VISIBLE
        }
    }

    private fun setupChips(genreList: List<Genre>) {
        for (genre in genreList) {
            val chip = Chip(requireContext())
            chip.text = genre.value
            binding.chipGroupGenres.addView(chip)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}