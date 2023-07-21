package com.ilhomsoliev.rikandmortytest.feature.locations.presentation.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilhomsoliev.rikandmortytest.core.fragments.repeatWhenViewStarted
import com.ilhomsoliev.rikandmortytest.databinding.FragmentDetailedLocationBinding
import com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.main.list.CharactersAdapter
import com.ilhomsoliev.rikandmortytest.feature.locations.viewmodel.DetailedLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DetailedLocationFragment : Fragment() {

    private val viewModel by viewModels<DetailedLocationViewModel>()
    private lateinit var binding: FragmentDetailedLocationBinding
    private val args: DetailedLocationFragmentArgs by navArgs()
    lateinit var charactersAdapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedLocationBinding.inflate(inflater, container, false)
        viewModel.getLocationById(args.locationId)

        setupRecyclerView()

        charactersAdapter.setOnItemClickListener { item ->
            val action =
                DetailedLocationFragmentDirections.actionDetailedLocationFragmentToDetailedCharacterFragment(
                    item.id
                )
            findNavController().navigate(action)
        }

        binding.detailedLocationArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        observeUiState()
        return binding.root
    }

    private fun observeUiState() = repeatWhenViewStarted {
        viewModel.state.collectLatest { state ->
            with(binding) {
                when {
                    state.isLoading -> {
                        detailedLocationProgressBar.visibility = View.VISIBLE
                        detailedLocationBody.visibility = View.INVISIBLE
                    }

                    !state.error.isNullOrEmpty() -> {
                        detailedLocationProgressBar.visibility = View.GONE
                    }

                    else -> {
                        detailedLocationProgressBar.visibility = View.GONE

                        state.location?.let { location ->
                            detailedLocationBody.visibility = View.VISIBLE

                            detailedLocationNameTextView.text = location.name
                            detailedLocationTypeTextView.text = location.type
                            detailedLocationDimensionTextView.text = location.dimension
                        }
                        when {
                            state.isCharactersLoading -> {
                                detailedLocationCharacterProgressBar.visibility = View.VISIBLE
                            }

                            else -> {
                                detailedLocationCharacterProgressBar.visibility = View.GONE
                                charactersAdapter.differ.submitList(state.characters)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
        binding.detailedLocationCharactersRecyclerView.apply {
            adapter = charactersAdapter
            layoutManager = object : LinearLayoutManager(activity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }
}