package com.ilhomsoliev.rikandmortytest.feature.locations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.rikandmortytest.domain.Response
import com.ilhomsoliev.rikandmortytest.domain.model.CharacterModel
import com.ilhomsoliev.rikandmortytest.domain.model.LocationModel
import com.ilhomsoliev.rikandmortytest.domain.usecases.GetCharactersByRequest
import com.ilhomsoliev.rikandmortytest.domain.usecases.GetLocationByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailedLocationState(
    val location: LocationModel? = null,
    val characters: List<CharacterModel> = emptyList(),
    val isLoading: Boolean = false,
    val isCharactersLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class DetailedLocationViewModel @Inject constructor(
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
    private val getCharactersByRequest: GetCharactersByRequest,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailedLocationState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailedLocationState()
    )

    fun getLocationById(id: Int) {
        viewModelScope.launch {
            getLocationByIdUseCase(id)
                .catch {
                    _state.emit(
                        _state.value.copy(
                            isLoading = false,
                            error = it.message.toString()
                        )
                    )
                }
                .collect { response ->
                    when (response) {
                        is Response.Success -> {
                            _state.emit(
                                _state.value.copy(
                                    location = response.data,
                                    isLoading = false
                                )
                            )
                            // Get Episodes
                            if(response.data.residents.isNotEmpty()) {
                                val request = response.data.residents.take(5).joinToString(
                                    separator = ",",
                                    prefix = "[",
                                    postfix = "]"
                                ) { getCharactersAfterLastSlash(it) }
                                getCharacters(request)
                            }
                        }

                        is Response.Loading -> {
                            _state.emit(
                                _state.value.copy(
                                    isLoading = true
                                )
                            )
                        }

                        is Response.Error -> {
                            _state.emit(
                                _state.value.copy(
                                    isLoading = false,
                                    error = response.message
                                )
                            )
                        }
                    }
                }
        }
    }

    private suspend fun getCharacters(request: String) {
        getCharactersByRequest(request).catch {
            _state.emit(
                _state.value.copy(
                    isLoading = false,
                    error = it.message.toString()
                )
            )
        }.collect { response ->
            when (response) {
                is Response.Success -> {
                    _state.emit(
                        _state.value.copy(
                            characters = response.data,
                            isCharactersLoading = false
                        )
                    )
                }

                is Response.Loading -> {
                    _state.emit(
                        _state.value.copy(
                            isCharactersLoading = true
                        )
                    )
                }

                is Response.Error -> {
                    _state.emit(
                        _state.value.copy(
                            isCharactersLoading = false,
                            error = response.message
                        )
                    )
                }
            }
        }
    }
}

fun getCharactersAfterLastSlash(input: String): String {
    val lastSlashIndex = input.lastIndexOf('/')
    return if (lastSlashIndex >= 0 && lastSlashIndex < input.length - 1) {
        input.substring(lastSlashIndex + 1)
    } else {
        ""
    }
}