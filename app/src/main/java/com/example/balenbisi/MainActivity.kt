package com.example.balenbisi

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.balenbisi.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    private lateinit var bikeStations: MutableList<BikeStation>
    private lateinit var adapter: BikeStationAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "valenbisi-db"
        ).build()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        bikeStations = mutableListOf()
        val recyclerView: RecyclerView = findViewById(R.id.rvBikeStations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BikeStationAdapter(bikeStations)
        recyclerView.adapter = adapter

        fetchBikeStations()
    }

    private fun fetchBikeStations() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getBikeStations()
                val stations = response.results.map { station ->
                    BikeStation(
                        number = station.number,
                        name = station.address,
                        free_bikes = station.available,
                        latitude = station.position?.lat ?: 0.0,
                        longitude = station.position?.lon ?: 0.0
                    )
                }

                withContext(Dispatchers.Main) {
                    bikeStations.clear()
                    bikeStations.addAll(stations)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Show error message
                    Toast.makeText(
                        this@MainActivity,
                        "Error fetching data: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()

                    // No longer trying to load from local data
                    // Just show an empty list or add some dummy data if needed
                    bikeStations.clear()

                    // Optionally add a dummy item to indicate error state
                    bikeStations.add(
                        BikeStation(
                            number = -1,
                            name = "No data available. Pull to refresh.",
                            free_bikes = 0,
                            latitude = 0.0,
                            longitude = 0.0
                        )
                    )

                    adapter.notifyDataSetChanged()
                }
            }
        }
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