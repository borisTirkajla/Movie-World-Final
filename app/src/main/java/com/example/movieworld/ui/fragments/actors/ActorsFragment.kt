package com.example.movieworld.ui.fragments.actors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieworld.adapters.ActorsListAdapter
import com.example.movieworld.databinding.FragmentActorsBinding
import com.example.movieworld.util.NetworkResult
import com.example.movieworld.viewmodels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorsFragment : Fragment() {

    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private var _binding: FragmentActorsBinding? = null

    private val binding get() = _binding!!
    private val mAdapter by lazy {
        ActorsListAdapter {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentActorsBinding.inflate(inflater, container, false).apply {
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
                    networkResult.data?.actorList?.let {
                        mAdapter.setData(it)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.apply {
            adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}