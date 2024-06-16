package com.capstone.hibeauty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.R

data class Prediction(val skinType: String, val percentage: Float)

class HistoryAdapter(private val predictions: List<Prediction>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val skinTypeTextView: TextView = view.findViewById(R.id.skinTypeTextView)
        val percentageTextView: TextView = view.findViewById(R.id.percentageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prediction = predictions[position]
        holder.skinTypeTextView.text = prediction.skinType
        holder.percentageTextView.text = String.format("%.2f%%", prediction.percentage * 100)
    }

    override fun getItemCount(): Int = predictions.size
}