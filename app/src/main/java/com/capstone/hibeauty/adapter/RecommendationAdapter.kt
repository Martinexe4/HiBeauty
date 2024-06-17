package com.capstone.hibeauty.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.ProductRecommendation
import com.capstone.hibeauty.scan.DetailRecommendationActivity

class RecommendationAdapter(private val context: Context) : ListAdapter<ProductRecommendation, RecommendationAdapter.RecommendationViewHolder>(RecommendationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = getItem(position)
        holder.bind(recommendation)
    }

    inner class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView= itemView.findViewById(R.id.cardView)
        private val nameTextView: TextView = itemView.findViewById(R.id.recommendationNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.recommendationDescriptionTextView)


        fun bind(recommendation: ProductRecommendation) {
            nameTextView.text = recommendation.name
            descriptionTextView.text = recommendation.description


            cardView.setOnClickListener {
                val intent = Intent(context, DetailRecommendationActivity::class.java).apply {
                    putExtra("recommendationId", recommendation.id)
                    putExtra("recommendationName", recommendation.name)
                    putExtra("recommendationDescription", recommendation.description)
                    putExtra("recommendationIngredients", recommendation.ingridients)
                    putExtra("recommendationLink", recommendation.link)
                }
                context.startActivity(intent)
            }
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