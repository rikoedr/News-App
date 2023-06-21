package com.android.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.model.SavedModel

class SavedAdapter(private var savedNewsList: MutableList<SavedModel>, private val context: Context)
    : RecyclerView.Adapter<SavedAdapter.SavedNewsVH>(){

    private lateinit var onListener: OnSavedItemClickListener

    interface OnSavedItemClickListener{
        fun onSavedItemClick(position: Int, source: String, title: String, publishedAt: String, urlToOpen: String)
    }

    fun setOnItemClickListener(listener: OnSavedItemClickListener){
        onListener = listener
    }

    inner class SavedNewsVH(itemView: View, listener: OnSavedItemClickListener): RecyclerView.ViewHolder(itemView){
        private val tvSavedSource = itemView.findViewById<TextView>(R.id.tv_saved_news_source)
        private val tvSavedTitle = itemView.findViewById<TextView>(R.id.tv_saved_news_title)
        private val tvSavedURL = itemView.findViewById<TextView>(R.id.tv_saved_news_url)

        private lateinit var articleSource: String
        private lateinit var articleTitle: String
        private lateinit var articlePublishedAt: String
        private lateinit var urlToOpen: String

        fun setArticleData(source: String, title: String, publishedAt: String, url: String){
            this.articleSource = source
            this.articleTitle = title
            this.articlePublishedAt = publishedAt
            this.urlToOpen = url
        }

        init{
            itemView.setOnClickListener {
                listener.onSavedItemClick(adapterPosition, articleSource, articleTitle, articlePublishedAt, urlToOpen)
            }
        }

        fun bind(savedNews: SavedModel){
            val id = savedNews.id
            val source = savedNews.source
            val title = savedNews.title
            val publishedAt = savedNews.publishedAt
            val url = savedNews.url

            //SET CARD SOURCE & TITLE
            tvSavedSource.text = "$source • $publishedAt"
            tvSavedTitle.text = if (title.length > 60) "${title.substring(0, 60)}..." else title
            tvSavedURL.text = savedNews.url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_saved_news, parent, false)
        return SavedNewsVH(view, onListener)
    }

    override fun onBindViewHolder(holder: SavedNewsVH, position: Int) {
        holder.setArticleData(savedNewsList[position].source,
            savedNewsList[position].title,
            savedNewsList[position].publishedAt,
            savedNewsList[position].url)

        holder.bind(savedNewsList[position])
    }

    override fun getItemCount(): Int = savedNewsList.size
}