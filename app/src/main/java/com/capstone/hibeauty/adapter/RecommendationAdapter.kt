package com.capstone.hibeauty.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.Recommendation

class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val recommendations = mutableListOf<Recommendation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        holder.bind(recommendation)
    }

    override fun getItemCount(): Int = recommendations.size

    fun submitList(list: List<Recommendation>) {
        recommendations.clear()
        recommendations.addAll(list)
        notifyDataSetChanged()
    }

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.recommendationName)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.recommendationDescription)

        fun bind(recommendation: Recommendation) {
            nameTextView.text = recommendation.name
            descriptionTextView.text = recommendation.description
        }
    }
}