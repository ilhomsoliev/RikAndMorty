package com.ilhomsoliev.rikandmortytest.data.network.model.episode

data class EpisodeResponse(
    val air_date: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)