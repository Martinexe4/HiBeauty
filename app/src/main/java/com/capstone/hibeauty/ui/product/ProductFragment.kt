package com.capstone.hibeauty.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.HorizontalProductAdapter
import com.capstone.hibeauty.adapter.VerticalProductAdapter
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.Product
import com.capstone.hibeauty.api.ProductResponse
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragment : Fragment() {

    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var verticalRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var horizontalAdapter: HorizontalProductAdapter
    private lateinit var verticalAdapter: VerticalProductAdapter
    private var products: List<Product> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        horizontalRecyclerView = view.findViewById(R.id.horizontal_recycler_view)
        verticalRecyclerView = view.findViewById(R.id.vertical_recycler_view)
        searchView = view.findViewById(R.id.search_view)

        horizontalAdapter = HorizontalProductAdapter(emptyList())
        verticalAdapter = VerticalProductAdapter(emptyList())

        horizontalRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        horizontalRecyclerView.adapter = horizontalAdapter

        verticalRecyclerView.layoutManager = LinearLayoutManager(context)
        verticalRecyclerView.adapter = verticalAdapter

        fetchProducts()

        setupSearchView()

        return view
    }

    private fun fetchProducts() {
        val apiService = ApiConfig.apiService
        val token = SharedPreferenceUtil.getToken(requireContext())
        Log.d("ProductFragment", "User token: $token")

        if (token != null) {
            val call = apiService.getAllProducts("Bearer $token")

            call.enqueue(object : Callback<ProductResponse> {
                override fun onResponse(
                    call: Call<ProductResponse>,
                    response: Response<ProductResponse>
                ) {
                    if (response.isSuccessful) {
                        val productResponse = response.body()

                        if (productResponse != null) {
                            products = productResponse.data

                            Log.d("ProductFragment", "Fetched products: $products")

                            horizontalAdapter.updateData(products.take(5))
                            verticalAdapter.updateData(products.drop(5))
                        } else {
                            showToast(getString(R.string.fetch_products_failed_null))
                        }
                    } else {
                        showToast(getString(R.string.fetch_products_failed_code, response.code()))
                    }
                }

                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    showToast(getString(R.string.fetch_products_failed_generic, t.message))
                }
            })
        } else {
            showToast(getString(R.string.token_not_found))
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterProducts(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterProducts(it) }
                return false
            }
        })
    }

    private fun filterProducts(query: String) {
        val filteredProducts = products.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }

        Log.d("ProductFragment", "Filtered products: $filteredProducts for query: $query")

        horizontalAdapter.updateData(filteredProducts.take(5))
        verticalAdapter.updateData(filteredProducts.drop(5))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
