package com.ilhomsoliev.rikandmortytest.feature.locations.presentation.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilhomsoliev.rikandmortytest.R
import com.ilhomsoliev.rikandmortytest.databinding.ItemCharacterBinding
import com.ilhomsoliev.rikandmortytest.databinding.ItemLocationBinding
import com.ilhomsoliev.rikandmortytest.domain.model.LocationModel

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>() {

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<LocationModel>() {
        override fun areItemsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: LocationModel, newItem: LocationModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_location,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((LocationModel) -> Unit)? = null

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val binding = ItemLocationBinding.bind(holder.itemView)
        val location = differ.currentList[position]
        binding.apply {
            //Glide.with(root).load(location.image).into(characterImageItem)
            locationItemNameTextView.text = location.name

            root.setOnClickListener {
                onItemClickListener?.let { it(location) }
            }
        }
    }

    fun setOnItemClickListener(listener: (LocationModel) -> Unit) {
        onItemClickListener = listener
    }
}