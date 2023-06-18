package com.android.newsapp.adapter

import android.content.Context
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

class TopNewsAdapter(private val articles: List<Articles>, private val context: Context?)
    : RecyclerView.Adapter<TopNewsAdapter.TopHeadlinesVH>(){

    inner class TopHeadlinesVH(itemView: View): RecyclerView.ViewHolder(itemView){
        private val topHeadlinesThumbnail = itemView.findViewById<ImageView>(R.id.iv_top_headlines_thumbnail)
        private val topHeadlinesTitle = itemView.findViewById<TextView>(R.id.tv_top_headlines_title)
        private val topHeadlinesSource = itemView.findViewById<TextView>(R.id.tv_top_headlines_source)

        fun bind(articles: Articles){
            with(itemView){
                val urlToImage = articles.urlToImage
                val source = articles.source.name
                val publishedAt = TimeUtil.Formatter(articles.publishedAt)
                val title = articles.title

                // SET CARD SOURCE & TITLE
                topHeadlinesSource.text = "$source • $publishedAt"
                topHeadlinesTitle.text = title //if (title.length > 34) "${title.substring(0, 34)}..." else "$title..."

                // SET CARD THUMBNAIL IMAGE
                Glide.with(this)
                    .load(urlToImage)
                    .error(R.drawable.image_not_available)
                    .into(topHeadlinesThumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopHeadlinesVH {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_top_headlines, parent, false)
        return TopHeadlinesVH(view)
    }

    override fun onBindViewHolder(holder: TopHeadlinesVH, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size
}