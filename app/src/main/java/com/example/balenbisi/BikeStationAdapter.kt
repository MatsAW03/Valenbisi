package com.example.balenbisi

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BikeStationAdapter(private val bikeStations: List<BikeStation>) :
    RecyclerView.Adapter<BikeStationAdapter.BikeStationViewHolder>() {

    class BikeStationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stationName: TextView = view.findViewById(R.id.tvStationName)
        val availableBikes: TextView = view.findViewById(R.id.tvFreeBikes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeStationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bike_station, parent, false)
        return BikeStationViewHolder(view)
    }

    override fun onBindViewHolder(holder: BikeStationViewHolder, position: Int) {
        val station = bikeStations[position]
        holder.stationName.text = station.name
        holder.availableBikes.text = "Bicicletas disponibles: ${station.free_bikes}"

        // Cambiar color según cantidad de bicicletas disponibles
        val color = when {
            station.free_bikes > 10 -> Color.GREEN  // Más de 10 → Verde
            station.free_bikes in 5..10 -> Color.parseColor("#FFA500") // 5 a 10 → Naranja
            else -> Color.RED  // Menos de 5 → Rojo
        }

        holder.availableBikes.setTextColor(color)

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            try {
                val intent = Intent(holder.itemView.context, BikeStationDetailActivity::class.java)
                // Make sure station is serializable with all fields properly initialized
                intent.putExtra("BIKE_STATION", station)
                holder.itemView.context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                // If you have more robust error handling, you could show a Toast here
            }
        }
    }

    override fun getItemCount() = bikeStations.size
}