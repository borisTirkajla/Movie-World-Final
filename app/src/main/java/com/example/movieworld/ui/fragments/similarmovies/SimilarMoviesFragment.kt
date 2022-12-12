package com.example.movieworld.ui.fragments.similarmovies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieworld.adapters.SimilarMoviesAdapter
import com.example.movieworld.databinding.FragmentSimilarMoviesBinding
import com.example.movieworld.ui.fragments.details.DetailsFragmentDirections
import com.example.movieworld.util.NetworkResult
import com.example.movieworld.viewmodels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SimilarMoviesFragment"

@AndroidEntryPoint
class SimilarMoviesFragment : Fragment() {
    private val detailsViewModel: DetailsViewModel by activityViewModels()

    private var _binding: FragmentSimilarMoviesBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy {
        SimilarMoviesAdapter { id ->
            try {
                val action =
                    DetailsFragmentDirections.actionDetailsFragmentSelf(
                        id
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
        FragmentSimilarMoviesBinding.inflate(inflater, container, false).apply {
            _binding = this
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.detailsViewModel = detailsViewModel

        setupRecyclerView()

        detailsViewModel.movieResponse.observe(viewLifecycleOwner) { networkResult ->
            when (networkResult) {
                is NetworkResult.Error -> Unit
                is NetworkResult.Loading -> Unit
                is NetworkResult.Success -> {
                    binding.recyclerview.visibility = View.VISIBLE
                    networkResult.data?.similars?.let {
                        mAdapter.setData(it)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}