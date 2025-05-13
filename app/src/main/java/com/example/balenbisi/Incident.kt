package com.example.balenbisi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class Incident(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val bikeStationId: Int,  // Links to BikeStation
    val status: String,      // "Open", "Processing", "Closed"
    val type: String         // "Mechanical", "Electric", "Painting", "Masonry"
)