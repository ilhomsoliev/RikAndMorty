package com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.detailed.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilhomsoliev.rikandmortytest.R
import com.ilhomsoliev.rikandmortytest.databinding.ItemEpisodeBinding
import com.ilhomsoliev.rikandmortytest.domain.model.EpisodeModel

class EpisodesAdapter: RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    inner class EpisodeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<EpisodeModel>() {
        override fun areItemsTheSame(oldItem: EpisodeModel, newItem: EpisodeModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: EpisodeModel, newItem: EpisodeModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_episode,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((EpisodeModel) -> Unit)? = null

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val binding = ItemEpisodeBinding.bind(holder.itemView)
        val episode = differ.currentList[position]
        binding.apply {
            episodeItemNameTextView.text = episode.name
            episodeItemDateTextView.text = episode.airDate
            root.setOnClickListener {
                onItemClickListener?.let { it(episode) }
            }
        }
    }

    fun setOnItemClickListener(listener: (EpisodeModel) -> Unit) {
        onItemClickListener = listener
    }
}