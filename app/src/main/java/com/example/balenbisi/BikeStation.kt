package com.example.balenbisi

import java.io.Serializable

data class BikeStation(
    val number: Int,
    val name: String,
    val free_bikes: Int
) : Serializable