package com.capstone.hibeauty.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.capstone.hibeauty.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)

        firestore = Firebase.firestore

        binding.closeButton.setOnClickListener {
            finish()
        }

        loadHistory()
    }

    private fun loadHistory() {
        firestore.collection("results")
            .get()
            .addOnSuccessListener { result ->
                val historyList = result.map { document ->
                    HistoryItem(
                        id = document.id,
                        imageUri = document.getString("imageUri") ?: "",
                        category = document.getString("category") ?: "",
                        probability = document.getDouble("probability")?.toFloat() ?: 0f
                    )
                }.toMutableList()
                historyAdapter = HistoryAdapter(historyList, this::deleteHistoryItem)
                binding.recyclerViewHistory.adapter = historyAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun deleteHistoryItem(id: String) {
        firestore.collection("results").document(id)
            .delete()
            .addOnSuccessListener {
                historyAdapter.removeItem(id)
                showToast("Item deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                showToast("Error deleting item: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "HistoryActivity"
    }
}