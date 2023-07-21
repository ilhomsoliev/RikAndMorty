package com.ilhomsoliev.rikandmortytest.feature.locations.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.rikandmortytest.domain.Response
import com.ilhomsoliev.rikandmortytest.domain.model.LocationModel
import com.ilhomsoliev.rikandmortytest.domain.usecases.GetLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocationsState(
    val locations: List<LocationModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LocationsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocationsState()
    )

    init {
        viewModelScope.launch {
            getLocations()
        }
    }

    private suspend fun getLocations() {
        getLocationsUseCase()
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
                                locations = response.data,
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