package com.example.balenbisi

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bikeStations = getData()

        val recyclerView: RecyclerView = findViewById(R.id.rvBikeStations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BikeStationAdapter(bikeStations)
        recyclerView.adapter = adapter

        // Encontrar los botones
        val btnSortByName: Button = findViewById(R.id.btnSortByName)
        val btnSortByBikes: Button = findViewById(R.id.btnSortByBikes)

        // Listener para ordenar por nombre
        btnSortByName.setOnClickListener {
            bikeStations.sortBy { it.name }  // Ordena alfabéticamente por nombre
            adapter.notifyDataSetChanged()
        }

        // Listener para ordenar por bicis disponibles
        btnSortByBikes.setOnClickListener {
            bikeStations.sortByDescending { it.free_bikes }  // Ordena por número de bicis (descendente)
            adapter.notifyDataSetChanged()
        }
    }

    private fun getData(): MutableList<BikeStation> {  // ⬅ Cambiar List por MutableList
        val inputStream: InputStream = resources.openRawResource(R.raw.valenbisi)
        val jsonText = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonText)

        val bikeStationsList = mutableListOf<BikeStation>()  // ⬅ MutableList en vez de List

        for (i in 0 until jsonArray.length()) {
            val station: JSONObject = jsonArray.getJSONObject(i)
            val number = station.getInt("number")
            val name = station.getString("address")
            val freeBikes = station.getInt("available")

            bikeStationsList.add(BikeStation(number, name, freeBikes))
        }

        return bikeStationsList
    }
}



