package com.capstone.hibeauty.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.hibeauty.databinding.FragmentArticleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.capstone.hibeauty.adapter.ArticleAdapter
import com.capstone.hibeauty.api.ApiConfig
import com.capstone.hibeauty.api.ApiService
import com.capstone.hibeauty.api.ArticlesItem
import com.capstone.hibeauty.api.NewsResponse

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: ArticleAdapter
    private lateinit var apiService: ApiService
    private var allArticles: List<ArticlesItem?> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        newsAdapter = ArticleAdapter(emptyList())
        binding.recyclerView.adapter = newsAdapter

        apiService = ApiConfig.getApiServiceNews()

        fetchNews()

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    filterArticles(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterArticles(newText)
                }
                return true
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchNews()
        }
    }

    private fun fetchNews() {
        binding.swipeRefreshLayout.isRefreshing = true
        val call = apiService.getNews()
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val articles = newsResponse?.articles
                    if (articles != null) {
                        allArticles = articles
                        if (isAdded) {
                            newsAdapter.updateData(articles)
                        }
                    }
                } else {
                    handleError(response.message())
                }
                if (isAdded) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                handleError(t.message ?: "Unknown error")
                if (isAdded) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    private fun filterArticles(query: String) {
        val filteredArticles = allArticles.filter {
            it?.title?.contains(query, true) == true ||
                    it?.description?.contains(query, true) == true
        }
        if (isAdded) {
            newsAdapter.updateData(filteredArticles)
        }
    }

    private fun handleError(errorMessage: String) {
        if (isAdded) {
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}