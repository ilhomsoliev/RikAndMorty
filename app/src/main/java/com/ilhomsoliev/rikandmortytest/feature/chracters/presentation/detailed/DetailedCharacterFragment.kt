package com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ilhomsoliev.rikandmortytest.core.fragments.repeatWhenViewStarted
import com.ilhomsoliev.rikandmortytest.databinding.FragmentDetailedCharacterBinding
import com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.detailed.list.EpisodesAdapter
import com.ilhomsoliev.rikandmortytest.feature.chracters.viewmodel.DetailedCharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DetailedCharacterFragment : Fragment() {

    private val viewModel by viewModels<DetailedCharacterViewModel>()
    private lateinit var binding: FragmentDetailedCharacterBinding
    private val args: DetailedCharacterFragmentArgs by navArgs()
    lateinit var episodeAdapter: EpisodesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedCharacterBinding.inflate(inflater, container, false)
        viewModel.getCharacterById(args.characterId)

        setupRecyclerView()

        episodeAdapter.setOnItemClickListener { item -> }

        binding.detailedCharacterArrowBack.setOnClickListener {
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
                        detailedCharacterProgressBar.visibility = View.VISIBLE
                        detailedCharacterBody.visibility = View.INVISIBLE
                    }

                    !state.error.isNullOrEmpty() -> {
                        detailedCharacterProgressBar.visibility = View.GONE
                    }

                    else -> {
                        detailedCharacterProgressBar.visibility = View.GONE

                        state.character?.let { character ->
                            detailedCharacterBody.visibility = View.VISIBLE
                            Glide.with(root).load(character.image)
                                .into(detailedCharacterAvatarImageView)
                            detailedCharacterNameTextView.text = character.name
                            detailedCharacterGenderTextView.text = character.gender
                            detailedCharacterSpeciesTextView.text = character.species
                            detailedCharacterLocationTextView.text = character.location
                        }
                        when {
                            state.isEpisodesLoading -> {
                                detailedCharacterEpisodesProgressBar.visibility = View.VISIBLE
                            }

                            else -> {
                                detailedCharacterEpisodesProgressBar.visibility = View.GONE
                                episodeAdapter.differ.submitList(state.episodes)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        episodeAdapter = EpisodesAdapter()
        binding.detailedCharacterLocationRecyclerView.apply {
            adapter = episodeAdapter
            layoutManager = object : LinearLayoutManager(activity) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }
}