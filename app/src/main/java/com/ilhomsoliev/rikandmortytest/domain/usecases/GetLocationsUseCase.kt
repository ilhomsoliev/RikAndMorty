package com.ilhomsoliev.rikandmortytest.domain.usecases

import com.ilhomsoliev.rikandmortytest.data.repository.Repository
import com.ilhomsoliev.rikandmortytest.domain.Response
import com.ilhomsoliev.rikandmortytest.domain.model.LocationModel
import com.ilhomsoliev.rikandmortytest.domain.model.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(): Flow<Response<List<LocationModel>>> = flow {
        emit(Response.Loading())
        try {
            emit(Response.Success(repository.getLocations().map { it.map() }))
        } catch (e: Exception) {
            emit(Response.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

}