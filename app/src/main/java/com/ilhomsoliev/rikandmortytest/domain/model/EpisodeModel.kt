package com.ilhomsoliev.rikandmortytest.domain.model

import com.ilhomsoliev.rikandmortytest.data.network.model.episode.EpisodeResponse

data class EpisodeModel(
    val airDate: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)

fun EpisodeResponse.map() = EpisodeModel(
    airDate = air_date,
    characters = characters,
    created = created,
    episode = episode,
    id = id,
    name = name,
    url = url,
)