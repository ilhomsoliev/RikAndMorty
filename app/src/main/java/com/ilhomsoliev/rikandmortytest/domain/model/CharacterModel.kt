package com.ilhomsoliev.rikandmortytest.domain.model

import com.ilhomsoliev.rikandmortytest.data.network.model.character.CharacterResponse

data class CharacterModel(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: String,
    val name: String,
    val origin: String,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)

fun CharacterResponse.map() = CharacterModel(
    created = created,
    episode = episode,
    gender = gender,
    id = id,
    image = image,
    location = location.name,
    name = name,
    origin = origin.name,
    species = species,
    status = status,
    type = type,
    url = url,
)