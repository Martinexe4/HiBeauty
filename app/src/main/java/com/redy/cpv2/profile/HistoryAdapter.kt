package com.redy.cpv2.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redy.cpv2.R

data class HistoryItem(
    val id: String,
    val imageUri: String,
    val category: String,
    val probability: Float
)

class HistoryAdapter(
    private val historyList: MutableList<HistoryItem>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewHistory)
        val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        val probabilityTextView: TextView = itemView.findViewById(R.id.textViewProbability)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyList[position]
        Glide.with(holder.imageView.context)
            .load(historyItem.imageUri)
            .into(holder.imageView)
        holder.categoryTextView.text = historyItem.category
        holder.probabilityTextView.text =
            holder.itemView.context.getString(R.string.probability_format, historyItem.probability)

        holder.deleteButton.setOnClickListener {
            onDeleteClick(historyItem.id)
        }
    }

    override fun getItemCount() = historyList.size

    fun removeItem(id: String) {
        val index = historyList.indexOfFirst { it.id == id }
        if (index != -1) {
            historyList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}