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
import androidx.viewpager2.widget.ViewPager2
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
    private lateinit var mainViewPager: ViewPager2

    private lateinit var homeContentLayout: LinearLayout
    private lateinit var allNewsLayout: LinearLayout
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var tvHomeHeader: TextView

    private var rvAllNews: RecyclerView? = null
    private var rvHeadlinesNews: RecyclerView? = null
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var headlinesNewsAdapter: TopNewsAdapter
    private lateinit var fragmentContext: Context

    private var articlesPage = 1

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
        val activity = requireActivity()
        mainViewPager = activity.findViewById(R.id.vp_content)
        homeContentLayout = view.findViewById(R.id.ll_home)
        allNewsLayout = view.findViewById(R.id.ll_all_news)
        linearLayoutManager = LinearLayoutManager(allNewsLayout.context)

        tvHomeHeader = view.findViewById(R.id.tv_home_news_header)

        // INIT HEADLINES RECYCLERVIEW
        initHeadlinesRV()


        // GET HEADLINES NEWS DATA
        getHeadlinesNews()
        getAllNews(true, articlesPage)

        // INFINITE SCROLLING
        setupInfiniteScroll()
    }

    // METHOD TO INITIALIZE RECYCLERVIEW
    private fun initHeadlinesRV(){
        rvHeadlinesNews = view?.findViewById(R.id.rv_top_news)
        rvHeadlinesNews?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvAllNews = view?.findViewById(R.id.rv_all_news)
        rvAllNews?.layoutManager = linearLayoutManager
    }

    // METHOD TO COLLECT NEWS DATA FROM API AND SET TO ADAPTERS
    private fun getHeadlinesNews(){
        NewsClient.newsAPI.getHeadlines(ContractAPI.COUNTRY, ContractAPI.API_KEY, 1).enqueue(object : Callback<NewsModel>{
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                if(response.body()?.articles != null){
                    val articlesList = response.body()?.articles as MutableList<Articles>
                    val topArticlesList = if (articlesList.size > 10) articlesList.take(10) else articlesList
                    headlinesNewsAdapter = TopNewsAdapter(topArticlesList, fragmentContext)
                    rvHeadlinesNews?.adapter = headlinesNewsAdapter

                    // Remove default background if the api response successfully
                    setFragmentBackground(false)

                    // Set-up news viewer if the api response successfully
                    headlinesNewsViewer()

                    // Prioritize headlines scroll view
                    headlinesScrollViewPrioritize()
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {

            }
        })
    }

    private fun getAllNews(isNewLoad: Boolean, page: Int){
        NewsClient.exploreAPI.getEverythingNews(ContractAPI.EVERYTHING, ContractAPI.API_KEY, ContractAPI.PAGE_SIZE_EXPLORE, page).enqueue(object : Callback<NewsModel>{
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>){

                // Check if the request came from new load page or scrolling (infinite scroll)
                if(isNewLoad && response.body()?.articles != null){
                    val articlesList = response.body()?.articles as MutableList<Articles>
                    newsAdapter = NewsAdapter(articlesList, fragmentContext)
                    rvAllNews?.adapter = newsAdapter

                    // Remove default background if the api response successfully
                    setFragmentBackground(false)

                    // Set-up news viewer if the api response successfully
                    allNewsViewer()
                }
                if(!isNewLoad && response.body()?.articles !=null){
                    newsAdapter.addItems(response.body()?.articles as MutableList<Articles>)
                }

            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) { }
        })
    }

    // METHOD TO SETUP INFINITE SCROLLING
    private fun setupInfiniteScroll(){
        rvAllNews?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemsCount = linearLayoutManager.childCount - 1
                val pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()

                if (visibleItemsCount + pastVisibleItems == newsAdapter.itemCount){
                    if(articlesPage * 10 == newsAdapter.itemCount){
                        articlesPage++
                        getAllNews(false, articlesPage)
                    }
                }

            }
        })
    }

    // METHOD TO PRIORITIZE HEADLINES SCROLL VIEW SWIPE
    private fun headlinesScrollViewPrioritize(){
        rvHeadlinesNews?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    mainViewPager.requestDisallowInterceptTouchEvent(true)
                }
            }
        })
    }

    // METHOD TO INITIALIZE NEWSVIEWER CLIENT
    private fun headlinesNewsViewer(){
        // NewsViewer Client for Top News List
        headlinesNewsAdapter.setOnTopNewsItemClickListener(object : TopNewsAdapter.onTopNewsItemClickListener{
            override fun onTopNewsItemClickListener(position: Int, source: String, title: String, publishedAt: String, urlToOpen: String) {
                Log.i("check ini", "$position + $urlToOpen")
                val intent = Intent(fragmentContext, NewsViewerActivity::class.java)
                intent.putExtra("articleSource", source)
                intent.putExtra("articleTitle", title)
                intent.putExtra("articlePublishedAt", publishedAt)
                intent.putExtra("urlToOpen", urlToOpen)
                startActivity(intent)

                // Prioritize touch event
                mainViewPager.requestDisallowInterceptTouchEvent(true)
            }
        })
    }

    private fun allNewsViewer(){
        // NewsViewer Client for All News List
        newsAdapter.setOnItemClickListener(object : NewsAdapter.onNewsItemClickListener{
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

    // METHOD TO SET FRAGMENT BACKGROUND
    private fun setFragmentBackground(status: Boolean){
        if (status) {
            homeContentLayout.setBackgroundResource(R.drawable.home_background_loading)
            tvHomeHeader.visibility = View.GONE
        }
        else {
            homeContentLayout.setBackgroundResource(R.drawable.home_bg_white)
            tvHomeHeader.visibility = View.VISIBLE
        }
    }
}