package com.capstone.hibeauty.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.hibeauty.R
import com.capstone.hibeauty.adapter.RecommendationAdapter
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.ApiService
import com.capstone.hibeauty.api.RecommendationResponse
import com.capstone.hibeauty.databinding.FragmentRecommendationsBinding
import com.capstone.hibeauty.utils.SharedPreferenceUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        recommendationApi = ApiConfig.apiService
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
        recommendationApi.getRecommendations(skinId, "Bearer $token")
            .enqueue(object : Callback<RecommendationResponse> {
                override fun onResponse(
                    call: Call<RecommendationResponse>,
                    response: Response<RecommendationResponse>
                ) {
                    if (response.isSuccessful) {
                        val recommendations = response.body()?.data ?: emptyList()
                        recommendationAdapter.submitList(recommendations)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(requireContext(), "Failed to fetch recommendations: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}