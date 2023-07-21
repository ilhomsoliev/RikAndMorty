package com.ilhomsoliev.rikandmortytest.feature.locations.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilhomsoliev.rikandmortytest.core.fragments.repeatWhenViewStarted
import com.ilhomsoliev.rikandmortytest.databinding.FragmentLocationsBinding
import com.ilhomsoliev.rikandmortytest.feature.chracters.viewmodel.CharactersViewModel
import com.ilhomsoliev.rikandmortytest.feature.locations.presentation.main.list.LocationsAdapter
import com.ilhomsoliev.rikandmortytest.feature.locations.viewmodel.LocationsViewModel
import com.ilhomsoliev.rikandmortytest.feature.viewpager.ViewPagerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    private val viewModel by viewModels<LocationsViewModel>()
    private lateinit var binding: FragmentLocationsBinding
    lateinit var locationsAdapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        locationsAdapter.setOnItemClickListener { item ->
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToDetailedLocationFragment(item.id)
            findNavController().navigate(action)
        }

        observeUiState()

        return binding.root
    }

    private fun observeUiState() = repeatWhenViewStarted {
        viewModel.state.collectLatest { state ->
            locationsAdapter.differ.submitList(state.locations)
            if (state.isLoading) binding.locationsProgressBar.visibility = View.VISIBLE
            else binding.locationsProgressBar.visibility = View.GONE
        }
    }
    private fun setupRecyclerView() {
        locationsAdapter = LocationsAdapter()
        binding.locationsRecyclerView.apply {
            adapter = locationsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}