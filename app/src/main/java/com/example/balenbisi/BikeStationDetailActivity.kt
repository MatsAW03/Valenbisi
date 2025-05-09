package com.example.balenbisi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

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

        // Retrieve the BikeStation object from the intent
        val recievedStation = intent.getSerializableExtra("BIKE_STATION") as BikeStation

        station = recievedStation

        findViewById<TextView>(R.id.tvDetailStationName).text = station.name
        findViewById<TextView>(R.id.tvDetailFreeBikes).text = "Bicicletas disponibles: ${station.free_bikes}"

    }
}