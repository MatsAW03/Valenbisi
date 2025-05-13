package com.example.balenbisi

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Incident::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun incidentDao(): IncidentDao
}