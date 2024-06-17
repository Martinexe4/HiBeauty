package com.capstone.hibeauty.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.ProductRecommendation

class RecommendationAdapter : ListAdapter<ProductRecommendation, RecommendationAdapter.RecommendationViewHolder>(RecommendationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = getItem(position)
        holder.bind(recommendation)
    }

    inner class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.recommendationNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.recommendationDescriptionTextView)
        private val ingredientsTextView: TextView = itemView.findViewById(R.id.recommendationIngredientsTextView)
        private val linkTextView: TextView = itemView.findViewById(R.id.recommendationLinkTextView)

        fun bind(recommendation: ProductRecommendation) {
            nameTextView.text = recommendation.name
            descriptionTextView.text = recommendation.description
            ingredientsTextView.text = "Ingredients: ${recommendation.ingridients}"
            linkTextView.text = "Link: ${recommendation.link}"
        }
    }

    class RecommendationDiffCallback : DiffUtil.ItemCallback<ProductRecommendation>() {
        override fun areItemsTheSame(oldItem: ProductRecommendation, newItem: ProductRecommendation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductRecommendation, newItem: ProductRecommendation): Boolean {
            return oldItem == newItem
        }
    }
}