package com.example.balenbisi.network

data class BikeStationResponse(
    val results: List<BikeStationData>
) {
    data class BikeStationData(
        val number: Int,
        val address: String,
        val available: Int,
        val position: Position?
    ) {
        data class Position(
            val lat: Double,
            val lon: Double
        )
    }
}