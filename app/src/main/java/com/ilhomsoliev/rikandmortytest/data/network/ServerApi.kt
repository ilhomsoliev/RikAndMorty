package com.ilhomsoliev.rikandmortytest.data.network

import com.ilhomsoliev.rikandmortytest.data.network.model.character.CharacterResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.character.CharactersResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.episode.EpisodeResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.location.LocationResponse
import com.ilhomsoliev.rikandmortytest.data.network.model.location.LocationsResponse
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://rickandmortyapi.com/api/"

interface ServerApi {
    @GET("location")
    suspend fun getLocation(): LocationsResponse

    @GET("character")
    suspend fun getCharacters(): CharactersResponse

    @GET("character/{request}")
    suspend fun getCharacterByRequest(@Path("request") request: String): List<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterResponse

    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): LocationResponse

    @GET("episode/{request}")
    suspend fun getEpisodes(@Path("request") request: String): List<EpisodeResponse>

}