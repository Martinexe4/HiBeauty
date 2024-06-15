package com.capstone.hibeauty.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.HorizontalProductAdapter
import com.capstone.hibeauty.authentication.ApiConfig
import android.widget.Toast
import com.capstone.hibeauty.adapter.VerticalProductAdapter
import com.capstone.hibeauty.authentication.Product
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragment : Fragment() {

    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var verticalRecyclerView: RecyclerView
    private lateinit var horizontalAdapter: HorizontalProductAdapter
    private lateinit var verticalAdapter: VerticalProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        horizontalRecyclerView = view.findViewById(R.id.horizontal_recycler_view)
        verticalRecyclerView = view.findViewById(R.id.vertical_recycler_view)

        horizontalAdapter = HorizontalProductAdapter(emptyList())
        verticalAdapter = VerticalProductAdapter(emptyList())

        // Set up horizontal RecyclerView
        horizontalRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        horizontalRecyclerView.adapter = horizontalAdapter

        // Set up vertical RecyclerView
        verticalRecyclerView.layoutManager = LinearLayoutManager(context)
        verticalRecyclerView.adapter = verticalAdapter

        // Fetch products
        fetchProducts()

        return view
    }

    private fun fetchProducts() {
        val apiService = ApiConfig.apiService
        val token = SharedPreferenceUtil.getToken(requireContext())

        if (token != null) {
            val call = apiService.getAllProducts("Bearer $token")

            call.enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        val products = response.body()
                        products?.let {
                            // Update adapters with fetched products
                            horizontalAdapter.updateData(it.take(3)) // Take first 3 products for horizontal RecyclerView
                            verticalAdapter.updateData(it.drop(3))  // Drop first 3 products for vertical RecyclerView
                        }
                    } else {
                        showToast("Failed to fetch products: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    showToast("Failed to fetch products: ${t.message}")
                }
            })
        } else {
            showToast("Token not found")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}