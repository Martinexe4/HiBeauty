package com.capstone.hibeauty.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.hibeauty.R
import com.capstone.hibeauty.api.Product
import com.capstone.hibeauty.databinding.ItemProductHorizontalBinding
import com.capstone.hibeauty.databinding.ItemProductVerticalBinding
import com.capstone.hibeauty.product.ProductDetailActivity

class HorizontalProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<HorizontalProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    fun updateData(newProductList: List<Product>) {
        productList = newProductList
        notifyDataSetChanged()
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

    inner class ViewHolder(private val binding: ItemProductHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productType.text = mapTypeIdToString(product.typeId)
            Glide.with(binding.productImage.context)
                .load(product.image)
                .placeholder(R.drawable.skincare_image_placeholder)
                .into(binding.productImage)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("product", product)
                }
                context.startActivity(intent)
            }
        }
    }
}

class VerticalProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<VerticalProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    fun updateData(newProductList: List<Product>) {
        productList = newProductList
        notifyDataSetChanged()
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

    inner class ViewHolder(private val binding: ItemProductVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productType.text = mapTypeIdToString(product.typeId)
            Glide.with(binding.productImage.context)
                .load(product.image) // Ensure this is the correct image URL
                .placeholder(R.drawable.skincare_image_placeholder) // Optionally add a placeholder
                .into(binding.productImage)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("product", product)
                }
                context.startActivity(intent)
            }
        }
    }
}