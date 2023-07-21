package com.ilhomsoliev.rikandmortytest.domain.repository

import com.ilhomsoliev.rikandmortytest.data.network.ServerApi
import com.ilhomsoliev.rikandmortytest.data.network.model.character.CharacterResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.episode.EpisodeResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.location.LocationResponse
import com.ilhomsoliev.rikandmortytest.data.repository.Repository

class RepositoryImpl(
    private val api: ServerApi
) : Repository {

    override suspend fun getLocations(): List<LocationResponse> =
        api.getLocation().results

    override suspend fun getCharacters(): List<CharacterResponse> = api.getCharacters().results
    override suspend fun getCharacterByRequest(request: String): List<CharacterResponse> = api.getCharacterByRequest(request)

    override suspend fun getCharacterById(id: Int): CharacterResponse = api.getCharacterById(id)

    override suspend fun getLocationById(id: Int): LocationResponse = api.getLocationById(id)

    override suspend fun getEpisodesByRequest(request: String): List<EpisodeResponse> =
        api.getEpisodes(request)

}