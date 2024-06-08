package com.capstone.hibeauty.article

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.hibeauty.R

class ArticleAdapter(private var articles: List<ArticlesItem?>) :
    RecyclerView.Adapter<ArticleAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)

        // Tambahkan listener klik untuk membuka DetailActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java)
            intent.putExtra("article", article)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateData(newArticles: List<ArticlesItem?>) {
        articles = newArticles
        notifyDataSetChanged()  // Memberi tahu RecyclerView bahwa data telah diperbarui
    }

//    fun updateData(newArticles: List<ArticlesItem?>) {
//        val diffCallback = ArticleDiffCallback(articles, newArticles)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        articles = newArticles
//        diffResult.dispatchUpdatesTo(this)  // Memperbarui hanya perubahan yang diperlukan
//    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val publishedAt: TextView = itemView.findViewById(R.id.published_at)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(article: ArticlesItem?) {
            title.text = article?.title
            description.text = article?.description
            publishedAt.text = article?.publishedAt
            article?.urlToImage?.let {
                Glide.with(itemView.context).load(it).into(image)
            }
        }
    }
}