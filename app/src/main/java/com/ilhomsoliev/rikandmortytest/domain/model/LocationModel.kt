package com.ilhomsoliev.rikandmortytest.domain.model

import com.ilhomsoliev.rikandmortytest.data.network.model.location.LocationResponse

data class LocationModel(
    val created: String,
    val dimension: String,
    val id: Int,
    val name: String,
    val residents: List<String>,
    val type: String,
    val url: String
)

fun LocationResponse.map() = LocationModel(
    created = created,
    dimension = dimension,
    id = id,
    name = name,
    residents = residents,
    type = type,
    url = url,
)