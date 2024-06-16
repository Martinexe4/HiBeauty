package com.capstone.hibeauty.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.api.Product
import com.capstone.hibeauty.databinding.ItemProductHorizontalBinding
import com.capstone.hibeauty.databinding.ItemProductVerticalBinding
import com.capstone.hibeauty.product.ProductDetailActivity

//import com.squareup.picasso.Picasso

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

    inner class ViewHolder(private val binding: ItemProductHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productType.text = product.description
//            Glide.with(binding.productImage.context)
//                .load(product.link)
//                .into(binding.productImage)

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

    inner class ViewHolder(private val binding: ItemProductVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productType.text = product.description
//            Glide.with(binding.productImage.context)
//                .load(product.link)
//                .into(binding.productImage)

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