package com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilhomsoliev.rikandmortytest.core.fragments.repeatWhenViewStarted
import com.ilhomsoliev.rikandmortytest.databinding.FragmentCharactersBinding
import com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.main.list.CharactersAdapter
import com.ilhomsoliev.rikandmortytest.feature.chracters.viewmodel.CharactersViewModel
import com.ilhomsoliev.rikandmortytest.feature.viewpager.ViewPagerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private val viewModel by viewModels<CharactersViewModel>()
    private lateinit var binding: FragmentCharactersBinding
    lateinit var charactersAdapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)

        setupRecyclerView()

        charactersAdapter.setOnItemClickListener { item ->
            val action =
                ViewPagerFragmentDirections.actionViewPagerFragmentToDetailedCharacterFragment(item.id)
            findNavController().navigate(action)
        }

        observeUiState()
        return binding.root
    }

    private fun observeUiState() = repeatWhenViewStarted {
        viewModel.state.collectLatest { state ->
            charactersAdapter.differ.submitList(state.characters)
            if (state.isLoading) binding.charactersProgressBar.visibility = View.VISIBLE
            else binding.charactersProgressBar.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
        binding.characterRecyclerView.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}