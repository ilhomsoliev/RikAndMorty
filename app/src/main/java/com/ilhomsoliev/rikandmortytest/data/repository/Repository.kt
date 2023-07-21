package com.ilhomsoliev.rikandmortytest.data.repository

import com.ilhomsoliev.rikandmortytest.data.network.model.character.CharacterResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.episode.EpisodeResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.location.LocationResponse

interface Repository {
    suspend fun getLocations(): List<LocationResponse>
    suspend fun getCharacters(): List<CharacterResponse>
    suspend fun getCharacterByRequest(request: String): List<CharacterResponse>
    suspend fun getCharacterById(id: Int): CharacterResponse
    suspend fun getLocationById(id: Int): LocationResponse
    suspend fun getEpisodesByRequest(request: String): List<EpisodeResponse>
}