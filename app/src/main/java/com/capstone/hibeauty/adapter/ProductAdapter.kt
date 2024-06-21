package com.capstone.hibeauty.adapter

import android.content.Context
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

class HorizontalProductAdapter(private val context: Context, private var productList: List<Product>) : RecyclerView.Adapter<HorizontalProductAdapter.ViewHolder>() {

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
            1 -> context.getString(R.string.moisturizer)
            2 -> context.getString(R.string.cleanser)
            3 -> context.getString(R.string.powder)
            4 -> context.getString(R.string.balm)
            5 -> context.getString(R.string.serum)
            6 -> context.getString(R.string.toner)
            7 -> context.getString(R.string.face_wash)
            8 -> context.getString(R.string.face_scrub)
            9 -> context.getString(R.string.eye_cream)
            10 -> context.getString(R.string.cream)
            11 -> context.getString(R.string.sunscreen)
            12 -> context.getString(R.string.micellar_water)
            else -> context.getString(R.string.unknown)
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
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("product", product)
                }
                context.startActivity(intent)
            }
        }
    }
}

class VerticalProductAdapter(private val context: Context, private var productList: List<Product>) : RecyclerView.Adapter<VerticalProductAdapter.ViewHolder>() {

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
            1 -> context.getString(R.string.moisturizer)
            2 -> context.getString(R.string.cleanser)
            3 -> context.getString(R.string.powder)
            4 -> context.getString(R.string.balm)
            5 -> context.getString(R.string.serum)
            6 -> context.getString(R.string.toner)
            7 -> context.getString(R.string.face_wash)
            8 -> context.getString(R.string.face_scrub)
            9 -> context.getString(R.string.eye_cream)
            10 -> context.getString(R.string.cream)
            11 -> context.getString(R.string.sunscreen)
            12 -> context.getString(R.string.micellar_water)
            else -> context.getString(R.string.unknown)
        }
    }

    inner class ViewHolder(private val binding: ItemProductVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productType.text = mapTypeIdToString(product.typeId)
            Glide.with(binding.productImage.context)
                .load(product.image)
                .placeholder(R.drawable.skincare_image_placeholder)
                .into(binding.productImage)

            itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("product", product)
                }
                context.startActivity(intent)
            }
        }
    }
}
