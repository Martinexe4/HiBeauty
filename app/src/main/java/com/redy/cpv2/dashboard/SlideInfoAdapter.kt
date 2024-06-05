package com.redy.cpv2.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redy.cpv2.R

class SlideInfoAdapter(private val slides: List<Map<String, Any>>) : RecyclerView.Adapter<SlideInfoAdapter.SlideViewHolder>() {

    class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.slideImage)
        val title: TextView = itemView.findViewById(R.id.slideTitle)
        val description: TextView = itemView.findViewById(R.id.slideDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_information, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val slide = slides[position]
        holder.title.text = slide["title"] as String
        holder.description.text = slide["description"] as String
        holder.image.setImageResource(slide["image"] as Int)
    }

    override fun getItemCount(): Int {
        return slides.size
    }
}