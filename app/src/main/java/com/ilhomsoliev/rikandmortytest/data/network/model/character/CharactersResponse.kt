package com.ilhomsoliev.rikandmortytest.data.network.model.character

import com.ilhomsoliev.rikandmortytest.data.network.model.shared.Info

data class CharactersResponse(
    val info: Info,
    val results: List<CharacterResponse>
)