package com.ilhomsoliev.rikandmortytest.feature.chracters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.rikandmortytest.domain.Response
import com.ilhomsoliev.rikandmortytest.domain.model.CharacterModel
import com.ilhomsoliev.rikandmortytest.domain.usecases.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharactersState(
    val characters: List<CharacterModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CharactersState()
    )

    init {
        viewModelScope.launch {
            getCharacters()
        }
    }

    private suspend fun getCharacters() {
        getCharactersUseCase()
            .catch {
                _state.emit(
                    _state.value.copy(
                        isLoading = false,
                        error = it.message
                    )
                )
            }
            .collect { response ->
                when (response) {
                    is Response.Success -> {
                        _state.emit(
                            _state.value.copy(
                                characters = response.data,
                                isLoading = false
                            )
                        )
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