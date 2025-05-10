package com.example.balenbisi

import java.io.Serializable

data class BikeStation(
    val number: Int,
    val name: String,
    val free_bikes: Int,
    val latitude: Double = 0.0,  // Default values in case they're not provided
    val longitude: Double = 0.0
) : Serializable