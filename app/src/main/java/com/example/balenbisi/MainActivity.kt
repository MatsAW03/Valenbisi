package com.example.balenbisi

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var bikeStations: MutableList<BikeStation>
    private lateinit var adapter: BikeStationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bikeStations = getData()

        val recyclerView: RecyclerView = findViewById(R.id.rvBikeStations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BikeStationAdapter(bikeStations)
        recyclerView.adapter = adapter
    }

    private fun getData(): MutableList<BikeStation> {
        val inputStream: InputStream = resources.openRawResource(R.raw.valenbisi)
        val jsonText = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonText)

        val bikeStationsList = mutableListOf<BikeStation>()

        for (i in 0 until jsonArray.length()) {
            try {
                val station: JSONObject = jsonArray.getJSONObject(i)
                val number = station.getInt("number")
                val name = station.getString("address")
                val freeBikes = station.getInt("available")

                var latitude = 0.0
                var longitude = 0.0

                try {
                    if (station.has("position")) {
                        val position = station.getJSONObject("position")
                        if (position.has("lat")) latitude = position.getDouble("lat")
                        if (position.has("lng")) longitude = position.getDouble("lng")
                    } else if (station.has("latitude") && station.has("longitude")) {
                        latitude = station.getDouble("latitude")
                        longitude = station.getDouble("longitude")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                bikeStationsList.add(BikeStation(number, name, freeBikes, latitude, longitude))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bikeStationsList
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort_name -> {
                bikeStations.sortBy { it.name }
                adapter.notifyDataSetChanged()
                true
            }
            R.id.menu_sort_bikes -> {
                bikeStations.sortByDescending { it.free_bikes }
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}