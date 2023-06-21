package com.android.newsapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.model.Articles
import com.android.newsapp.util.TimeUtil
import com.bumptech.glide.Glide

class NewsAdapter(private var articles: MutableList<Articles>, private val context: Context?)
    : RecyclerView.Adapter<NewsAdapter.HeadlineVH>() {

    private lateinit var onListener: onNewsItemClickListener
    interface onNewsItemClickListener{
        fun onNewsItemClickListener(position: Int, source: String, title: String, publishedAt: String, urlToOpen: String)
    }

    fun setOnItemClickListener(listener: onNewsItemClickListener){
        onListener = listener
    }

    inner class HeadlineVH(itemView: View, listener: onNewsItemClickListener): RecyclerView.ViewHolder(itemView){
        private val headlinesThumbnail = itemView.findViewById<ImageView>(R.id.iv_headlines_thumbnail)
        private val headlinesSource = itemView.findViewById<TextView>(R.id.tv_headlines_source)
        private val headlinesTitle = itemView.findViewById<TextView>(R.id.tv_headlines_title)

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
                listener.onNewsItemClickListener(adapterPosition, articleSource, articleTitle, articlePublishedAt, urlToOpen)
            }
        }
        fun bind(articles: Articles){
            with(itemView){
                val urlToImage = articles.urlToImage
                val source = articles.source.name
                val publishedAt = TimeUtil.Formatter(articles.publishedAt)
                val title = articles.title

                // SET CARD SOURCE & TITLE
                headlinesSource.text = "$source • $publishedAt"
                headlinesTitle.justificationMode
                headlinesTitle.text = if (title.length > 80) "${title.substring(0, 80)}..." else title

                // SET CARD THUMBNAIL IMAGE
                Glide.with(this)
                    .load(urlToImage)
                    .error(R.drawable.image_not_available)
                    .into(headlinesThumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_news, parent, false)
        return HeadlineVH(view, onListener)
    }

    override fun onBindViewHolder(holder: HeadlineVH, position: Int) {
        holder.setArticleData(articles[position].source.name,
            articles[position].title,
            articles[position].publishedAt,
            articles[position].url)

        holder.bind(articles[position])
        Log.i("News API", "Adapter Works!")
    }

    override fun getItemCount(): Int = articles.size

    fun addItems(moreArticles: List<Articles>){
        articles.addAll(moreArticles)
        notifyDataSetChanged()
    }
}