package com.example.balenbisi.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // The endpoint path should not include the base URL part
    @GET("catalog/datasets/valenbisi-disponibilitat-valenbisi-dsiponibilidad/records")
    suspend fun getBikeStations(
        @Query("limit") limit: Int = 100
    ): BikeStationResponse
}