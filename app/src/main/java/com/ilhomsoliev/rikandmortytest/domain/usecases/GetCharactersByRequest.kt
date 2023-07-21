package com.ilhomsoliev.rikandmortytest.domain.usecases

import com.ilhomsoliev.rikandmortytest.data.repository.Repository
import com.ilhomsoliev.rikandmortytest.domain.Response
import com.ilhomsoliev.rikandmortytest.domain.model.CharacterModel
import com.ilhomsoliev.rikandmortytest.domain.model.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCharactersByRequest @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(request: String = ""): Flow<Response<List<CharacterModel>>> = flow {
        emit(Response.Loading())
        try {
            emit(Response.Success(repository.getCharacterByRequest(request).map { it.map() }))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

}