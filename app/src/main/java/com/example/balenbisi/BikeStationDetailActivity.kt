package com.example.balenbisi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class BikeStationDetailActivity : AppCompatActivity() {
    private lateinit var station: BikeStation
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bike_station_detail)

        // Initialize Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "valenbisi-db"
        ).build()

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "Station Details"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve station data
        try {
            @Suppress("DEPRECATION")
            station = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("BIKE_STATION", BikeStation::class.java)
                    ?: throw Exception("No station data found")
            } else {
                (intent.getSerializableExtra("BIKE_STATION") as? BikeStation)
                    ?: throw Exception("No station data found")
            }

            // Update UI
            findViewById<TextView>(R.id.tvDetailStationName).text = station.name
            findViewById<TextView>(R.id.tvDetailFreeBikes).text = "Bicicletas disponibles: ${station.free_bikes}"

            // Set up FAB click listener
            findViewById<FloatingActionButton>(R.id.fabAddIncident).setOnClickListener {
                val intent = Intent(this, ReportIncidentActivity::class.java).apply {
                    putExtra("STATION_ID", station.number)
                    putExtra("STATION_NAME", station.name) // Add station name
                }
                startActivity(intent)
            }

            // Load and display incidents
            loadIncidents()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading station data", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadIncidents() {
        lifecycleScope.launch {
            try {
                val incidents = database.incidentDao().getIncidentsByStation(station.number)
                runOnUiThread {
                    val recyclerView = findViewById<RecyclerView>(R.id.rvIncidents)
                    recyclerView.layoutManager = LinearLayoutManager(this@BikeStationDetailActivity)
                    recyclerView.adapter = IncidentAdapter(incidents)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@BikeStationDetailActivity,
                        "Error loading incidents",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh incidents when returning from ReportIncidentActivity
        if (::station.isInitialized) {
            loadIncidents()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.menu_open_map -> {
                openStationInMaps()
                true
            }
            R.id.menu_report_incident -> {
                // Reuse the same code as the FAB click
                val intent = Intent(this, ReportIncidentActivity::class.java).apply {
                    putExtra("STATION_ID", station.number)
                    putExtra("STATION_NAME", station.name)
                }
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openStationInMaps() {
        try {
            val geoUri = if (station.latitude != 0.0 && station.longitude != 0.0) {
                Uri.parse("geo:${station.latitude},${station.longitude}?q=${Uri.encode(station.name)}")
            } else {
                Uri.parse("geo:0,0?q=${Uri.encode(station.name)}")
            }

            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                setPackage("com.google.android.apps.maps")
            }

            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(station.name)}")
                )
                startActivity(webIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error opening maps", Toast.LENGTH_SHORT).show()
        }
    }
}