package com.capstone.hibeauty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.api.HistoryItem
import com.capstone.hibeauty.databinding.ItemHistoryBinding

class HistoryAdapter(private var items: List<HistoryItem>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.historyItemId.text = item.id
            binding.historyItemPredictions.text = item.predictions.joinToString("\n") {
                "Prediction ID: ${it.skinType}, Percentage: ${it.percentage * 100}%"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HistoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}