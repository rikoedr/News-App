package com.android.newsapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.NewsViewerActivity
import com.android.newsapp.R
import com.android.newsapp.adapter.NewsAdapter
import com.android.newsapp.adapter.TopNewsAdapter
import com.android.newsapp.api.ContractAPI
import com.android.newsapp.api.NewsClient
import com.android.newsapp.model.Articles
import com.android.newsapp.model.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private lateinit var homeContentLayout: LinearLayout
    private lateinit var tvHomeHeader: TextView

    private var rvHeadlines: RecyclerView? = null
    private lateinit var headlinesAdapter: NewsAdapter
    private var rvTopHeadlines: RecyclerView? = null
    private lateinit var topHeadlinesAdapter: TopNewsAdapter

    private lateinit var fragmentContext: Context

    // BUILT-IN VARIABLE FRAGEMENTS
    private var param1: String? = null
    private var param2: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BUNDLE
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // INIT CONTENT LAYOUT
        homeContentLayout = view.findViewById(R.id.ll_home)
        tvHomeHeader = view.findViewById(R.id.tv_home_news_header)

        // INIT HEADLINES RECYCLERVIEW
        initHeadlinesRV()

        // GET HEADLINES NEWS DATA
        getHeadlinesNews()
    }

    private fun setHomeLoadingScreen(status: Boolean){
        when(status){
            true -> {
                homeContentLayout.setBackgroundResource(R.drawable.home_background_loading)
                tvHomeHeader.visibility = View.GONE
            }
            else -> {
                homeContentLayout.setBackgroundResource(R.drawable.home_bg_white)
                tvHomeHeader.visibility = View.VISIBLE
            }
        }
    }
    private fun initHeadlinesRV(){
        rvTopHeadlines = view?.findViewById(R.id.rv_top_headlines)
        rvTopHeadlines?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvHeadlines = view?.findViewById(R.id.rv_headlines)
        rvHeadlines?.layoutManager = LinearLayoutManager(context)
    }

    private fun headlinesNewsViewer(){
        headlinesAdapter.setOnItemClickListener(object : NewsAdapter.onNewsItemClickListener{
            override fun onNewsItemClickListener(position: Int, source: String, title: String, publishedAt: String, urlToOpen: String) {
                Log.i("check ini", "$position + $urlToOpen")
                val intent = Intent(fragmentContext, NewsViewerActivity::class.java)
                intent.putExtra("articleSource", source)
                intent.putExtra("articleTitle", title)
                intent.putExtra("articlePublishedAt", publishedAt)
                intent.putExtra("urlToOpen", urlToOpen)
                startActivity(intent)
            }

        })
    }

    private fun topHeadlinesNewsViewer(){
        topHeadlinesAdapter.setOnTopNewsItemClickListener(object : TopNewsAdapter.onTopNewsItemClickListener{
            override fun onTopNewsItemClickListener(position: Int, source: String, title: String, publishedAt: String, urlToOpen: String) {
                Log.i("check ini", "$position + $urlToOpen")
                val intent = Intent(fragmentContext, NewsViewerActivity::class.java)
                intent.putExtra("articleSource", source)
                intent.putExtra("articleTitle", title)
                intent.putExtra("articlePublishedAt", publishedAt)
                intent.putExtra("urlToOpen", urlToOpen)
                startActivity(intent)
            }
        })
    }

    private fun getHeadlinesNews(){
        NewsClient.headlinesAPI.getHeadlines(ContractAPI.COUNTRY, ContractAPI.API_KEY, 1).enqueue(object : Callback<NewsModel>{
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                // Troubleshooting
                Log.i("News API", "Status : API Connected \n Response : ${response.code()} ")

                // Collect Data From API
                val headlinesModel: NewsModel? = response.body()
                if(headlinesModel != null){
                    if(headlinesModel.articles.isNotEmpty()){
                        // Change background
                        setHomeLoadingScreen(false)

                        // Set Top Headlines Article
                        val topHeadlinesArticles = if (headlinesModel.articles.size > 5) headlinesModel.articles.take(5) else headlinesModel.articles
                        topHeadlinesAdapter = TopNewsAdapter(topHeadlinesArticles, context)
                        rvTopHeadlines?.adapter = topHeadlinesAdapter

                        // Set Headlines Article
                        val headlinesArticles = headlinesModel.articles.subList(5, headlinesModel.articles.size)
                        headlinesAdapter = NewsAdapter(headlinesArticles as MutableList<Articles>, context)
                        rvHeadlines?.adapter = headlinesAdapter

                        // SETUP NEWS VIEWER
                        headlinesNewsViewer()
                        topHeadlinesNewsViewer()

                        // Troubleshooting
                        Log.i("Home Fragment", "HOME FRAGMENT : ${headlinesModel.articles}")
                    }
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {

            }
        })
    }
}