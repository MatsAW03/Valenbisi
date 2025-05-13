package com.example.balenbisi

import androidx.room.*

@Dao
interface IncidentDao {

    @Insert
    suspend fun insert(incident: Incident)

    @Update
    suspend fun update(incident: Incident)

    @Delete
    suspend fun delete(incident: Incident)

    @Query("SELECT * FROM incidents WHERE bikeStationId = :stationId")
    suspend fun getIncidentsByStation(stationId: Int): List<Incident>

    @Query("SELECT * FROM incidents WHERE id = :incidentId")
    suspend fun getIncidentById(incidentId: Int): Incident?
}