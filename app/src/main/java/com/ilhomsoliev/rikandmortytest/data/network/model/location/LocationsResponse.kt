package com.ilhomsoliev.rikandmortytest.data.network.model.location

import com.ilhomsoliev.rikandmortytest.data.network.model.shared.Info

data class LocationsResponse(
    val info: Info,
    val results: List<LocationResponse>
)