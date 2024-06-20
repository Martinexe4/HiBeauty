package com.capstone.hibeauty.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        private val typeTextView: TextView = itemView.findViewById(R.id.recommendationTypeTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.recommendationImageView)

        fun bind(recommendation: ProductRecommendation) {
            nameTextView.text = recommendation.name
            typeTextView.text = mapTypeIdToString(recommendation.typeId)


            Glide.with(context)
                .load(recommendation.image)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)

            cardView.setOnClickListener {
                val intent = Intent(context, DetailRecommendationActivity::class.java).apply {
                    putExtra("recommendationId", recommendation.id)
                    putExtra("recommendationName", recommendation.name)
                    putExtra("recommendationDescription", recommendation.description)
                    putExtra("recommendationIngredients", recommendation.ingridients)
                    putExtra("recommendationLink", recommendation.link)
                    putExtra("recommendationImage", recommendation.image)
                    putExtra("recommendationType", recommendation.typeId)
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

    private fun mapTypeIdToString(typeId: Int): String {
        return when (typeId) {
            1 -> "Moisturizer"
            2 -> "Cleanser"
            3 -> "Powder"
            4 -> "Balm"
            5 -> "Serum"
            6 -> "Toner"
            7 -> "Face Wash"
            8 -> "Eye Cream"
            9 -> "Face Scrub"
            10 -> "Sunscreen"
            11 -> "Micellar Water"
            12 -> "Acne Spot"
            else -> "Unknown"
        }
    }
}