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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BikeStationDetailActivity : AppCompatActivity() {
    private lateinit var station: BikeStation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bike_station_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        try {
            @Suppress("DEPRECATION")
            station = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("BIKE_STATION", BikeStation::class.java)
                    ?: throw Exception("No station data found")
            } else {
                (intent.getSerializableExtra("BIKE_STATION") as? BikeStation)
                    ?: throw Exception("No station data found")
            }

            title = station.name
            findViewById<TextView>(R.id.tvDetailStationName).text = station.name
            findViewById<TextView>(R.id.tvDetailFreeBikes).text = "Bicicletas disponibles: ${station.free_bikes}"
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error cargando datos de la estación", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_open_maps -> {
                openStationInMaps()
                true
            }
            R.id.menu_report_incident -> {
                Toast.makeText(this, "Funcionalidad disponible próximamente", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openStationInMaps() {
        try {
            val geoUri = if (station.latitude != 0.0 && station.longitude != 0.0) {
                "geo:${station.latitude},${station.longitude}?q=${Uri.encode(station.name)}"
            } else {
                "geo:0,0?q=${Uri.encode(station.name)}"
            }

            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
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
            Toast.makeText(this, "Error al abrir el mapa", Toast.LENGTH_SHORT).show()
        }
    }
}