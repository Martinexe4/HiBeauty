package com.redy.cpv2.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redy.cpv2.R

class ProductFragment : Fragment() {

    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var verticalRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        horizontalRecyclerView = view.findViewById(R.id.horizontal_recycler_view)
        verticalRecyclerView = view.findViewById(R.id.vertical_recycler_view)

        // Set up horizontal RecyclerView
        horizontalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        horizontalRecyclerView.adapter = HorizontalProductAdapter(getHorizontalProducts())

        // Set up vertical RecyclerView
        verticalRecyclerView.layoutManager = LinearLayoutManager(context)
        verticalRecyclerView.adapter = VerticalProductAdapter(getVerticalProducts())

        return view
    }

    private fun getHorizontalProducts(): List<Product> {
        // Replace with real data fetching logic
        return listOf(
            Product(R.drawable.placeholder_image, "New Product 1", "$15", "Description for New Product 1", 4.5f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image)),
            Product(R.drawable.placeholder_image, "New Product 2", "$25", "Description for New Product 2", 4.0f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image)),
            Product(R.drawable.placeholder_image, "New Product 3", "$35", "Description for New Product 3", 3.5f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image))
        )
    }

    private fun getVerticalProducts(): List<Product> {
        // Replace with real data fetching logic
        return listOf(
            Product(R.drawable.placeholder_image, "New Product 4", "$45", "Description for New Product 4", 5.0f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image)),
            Product(R.drawable.placeholder_image, "New Product 5", "$55", "Description for New Product 5", 4.5f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image)),
            Product(R.drawable.placeholder_image, "New Product 6", "$65", "Description for New Product 6", 4.0f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image)),
            Product(R.drawable.placeholder_image, "New Product 7", "$75", "Description for New Product 7", 3.5f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image)),
            Product(R.drawable.placeholder_image, "New Product 8", "$85", "Description for New Product 8", 3.0f, listOf(R.drawable.placeholder_image, R.drawable.placeholder_image))
        )
    }
}