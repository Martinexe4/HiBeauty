package com.capstone.hibeauty.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.RecommendationAdapter
import com.capstone.hibeauty.api.ApiService
import com.capstone.hibeauty.databinding.FragmentRecommendationsBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendationsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRecommendationsBinding
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var recommendationApi: ApiService

    private lateinit var closeButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeButton = view.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dismiss()
        }

        setupRecyclerView()

        val skinId = arguments?.getString("skinId") ?: return
        val token = SharedPreferenceUtil.getToken(requireContext()) ?: return

        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-q4bx5v5sia-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recommendationApi = retrofit.create(ApiService::class.java)

        fetchRecommendations(skinId, token)
    }

    private fun setupRecyclerView() {
        recommendationAdapter = RecommendationAdapter(requireContext())
        binding.recommendationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendationAdapter
        }
    }


    private fun fetchRecommendations(skinId: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = recommendationApi.getRecommendations(skinId, "Bearer $token").execute()
                if (response.isSuccessful) {
                    val recommendations = response.body()?.data ?: emptyList()
                    withContext(Dispatchers.Main) {
                        recommendationAdapter.submitList(recommendations)
                    }
                } else {
                    // Log the response error message for debugging
                    val errorBody = response.errorBody()?.string()
                    Log.e("RecommendationActivity", "Error fetching recommendations: $errorBody")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Failed to fetch recommendations: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("RecommendationActivity", "Exception fetching recommendations", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}