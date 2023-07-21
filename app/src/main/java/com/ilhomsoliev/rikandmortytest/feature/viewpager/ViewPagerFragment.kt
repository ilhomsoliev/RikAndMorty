package com.ilhomsoliev.rikandmortytest.feature.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ilhomsoliev.rikandmortytest.databinding.FragmentViewPagerBinding
import com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.main.CharactersFragment
import com.ilhomsoliev.rikandmortytest.feature.locations.presentation.main.LocationsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root
        val tabLayout = binding.tabLayout

        val fragmentList = arrayListOf(
            CharactersFragment(),
            LocationsFragment(),
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position){
                0 -> "Characters"
                else -> "Locations"
            }
        }.attach()
        return view
    }

}