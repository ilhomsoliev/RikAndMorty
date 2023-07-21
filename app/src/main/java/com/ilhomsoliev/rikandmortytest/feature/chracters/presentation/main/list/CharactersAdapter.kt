package com.ilhomsoliev.rikandmortytest.feature.chracters.presentation.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilhomsoliev.rikandmortytest.R
import com.ilhomsoliev.rikandmortytest.databinding.ItemCharacterBinding
import com.ilhomsoliev.rikandmortytest.domain.model.CharacterModel

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_character,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((CharacterModel) -> Unit)? = null

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val binding = ItemCharacterBinding.bind(holder.itemView)
        val character = differ.currentList[position]
        binding.apply {
            Glide.with(root).load(character.image).into(characterImageItem)
            characterNameItem.text = character.name

            root.setOnClickListener {
                onItemClickListener?.let { it(character) }
            }
        }
    }

    fun setOnItemClickListener(listener: (CharacterModel) -> Unit) {
        onItemClickListener = listener
    }
}