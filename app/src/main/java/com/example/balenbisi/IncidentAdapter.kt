package com.example.balenbisi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncidentAdapter(private val incidents: List<Incident>) :
    RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    class IncidentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvIncidentName)
        val description: TextView = view.findViewById(R.id.tvIncidentDescription)
        val status: TextView = view.findViewById(R.id.tvIncidentStatus)
        val type: TextView = view.findViewById(R.id.tvIncidentType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incident, parent, false)
        return IncidentViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        val incident = incidents[position]
        holder.name.text = incident.name
        holder.description.text = incident.description
        holder.status.text = "Status: ${incident.status}"
        holder.type.text = "Type: ${incident.type}"
    }

    override fun getItemCount() = incidents.size
}