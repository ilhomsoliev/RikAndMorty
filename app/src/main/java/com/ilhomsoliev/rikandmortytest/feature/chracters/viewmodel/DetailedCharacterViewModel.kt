package com.ilhomsoliev.rikandmortytest.feature.chracters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.rikandmortytest.domain.Response
import com.ilhomsoliev.rikandmortytest.domain.model.CharacterModel
import com.ilhomsoliev.rikandmortytest.domain.model.EpisodeModel
import com.ilhomsoliev.rikandmortytest.domain.usecases.GetCharacterByIdUseCase
import com.ilhomsoliev.rikandmortytest.domain.usecases.GetEpisodesByRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailedCharacterState(
    val character: CharacterModel? = null,
    val episodes: List<EpisodeModel> = emptyList(),
    val isLoading: Boolean = false,
    val isEpisodesLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class DetailedCharacterViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val getEpisodesByRequestUseCase: GetEpisodesByRequestUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailedCharacterState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailedCharacterState()
    )

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            getCharacterByIdUseCase(id)
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
                                    character = response.data,
                                    isLoading = false
                                )
                            )
                            // Get Episodes
                            val request = response.data.episode.take(5).joinToString(
                                separator = ",",
                                prefix = "[",
                                postfix = "]"
                            ) { getCharactersAfterLastSlash(it) }
                            getEpisodes(request)
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

    private suspend fun getEpisodes(request: String) {
        getEpisodesByRequestUseCase(request).catch {
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
                            episodes = response.data,
                            isEpisodesLoading = false
                        )
                    )
                }

                is Response.Loading -> {
                    _state.emit(
                        _state.value.copy(
                            isEpisodesLoading = true
                        )
                    )
                }

                is Response.Error -> {
                    _state.emit(
                        _state.value.copy(
                            isEpisodesLoading = false,
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