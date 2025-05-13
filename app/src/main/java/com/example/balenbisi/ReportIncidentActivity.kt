package com.example.balenbisi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class ReportIncidentActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_incident)

        // Get station info from intent
        val stationId = intent.getIntExtra("STATION_ID", -1)
        val stationName = intent.getStringExtra("STATION_NAME") ?: "Unknown Station"

        // Display station name
        findViewById<TextView>(R.id.tvStationName).text = "Station: $stationName"

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "valenbisi-db"
        ).build()

        val saveButton = findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.etName).text.toString()
            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            val status = findViewById<Spinner>(R.id.spinnerStatus).selectedItem.toString()
            val type = findViewById<Spinner>(R.id.spinnerType).selectedItem.toString()

            if (name.isBlank() || description.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val incident = Incident(
                name = name,
                description = description,
                bikeStationId = stationId,
                status = status,
                type = type
            )

            lifecycleScope.launch {
                try {
                    database.incidentDao().insert(incident)
                    runOnUiThread {
                        Toast.makeText(
                            this@ReportIncidentActivity,
                            "Incident reported successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@ReportIncidentActivity,
                            "Error saving incident",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}