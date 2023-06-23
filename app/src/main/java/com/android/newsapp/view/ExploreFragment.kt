package com.android.newsapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.adapter.NewsAdapter
import com.android.newsapp.api.ContractAPI
import com.android.newsapp.api.NewsClient
import com.android.newsapp.model.Articles
import com.android.newsapp.model.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ExploreFragment : Fragment() {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var fragmentContext: Context
    private var rvExploreNews: RecyclerView? = null

    private var articlesPage = 1
    private var articlesCategory = ContractAPI.TECH

    private var param1: String? = null
    private var param2: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = requireContext()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // INIT HEADLINES RECYCLERVIEW
        linearLayoutManager = LinearLayoutManager(context)
        rvExploreNews = view?.findViewById(R.id.rv_explore_news)
        rvExploreNews?.layoutManager = linearLayoutManager

        // EXPLORE BAR
        setupExploreBar()

        // GET DEFAULT EXPLORE NEWS
        getExploreNews(true, articlesCategory, articlesPage)

        // INFINITE SCROLLING
        setupInfiniteScroll()
    }

    // METHOD TO SET-UP EXPLORE BAR (RADIO GROUP)
    private fun setupExploreBar(){
        val rgExplore = view?.findViewById<RadioGroup>(R.id.rg_explore_bar)
        var rbTech = view?.findViewById<RadioButton>(R.id.rb_explore_tech)
        rbTech?.isChecked = true

        rgExplore?.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_explore_health -> resetInfiniteScroll(ContractAPI.HEALTH)
                R.id.rb_explore_economy -> resetInfiniteScroll(ContractAPI.ECONOMY)
                R.id.rb_explore_sport -> resetInfiniteScroll(ContractAPI.SPORT)
                R.id.rb_explore_science -> resetInfiniteScroll(ContractAPI.SCIENCE)
                else -> resetInfiniteScroll(ContractAPI.TECH)
            }
        }
    }

    // METHOD TO SET-UP EXPLORE NEWS DATA LIST & APPLYING TO ADAPTER
    private fun getExploreNews(isNewLoad: Boolean, category: String, page: Int){
        NewsClient.exploreAPI.getEverythingNews(category, ContractAPI.API_KEY, ContractAPI.PAGE_SIZE_EXPLORE, page).enqueue(object : Callback<NewsModel>{
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {

                // Check if the request came from new load page or scrolling (infinite scroll)
                if(isNewLoad && response.body()?.articles != null){
                    val articlesList = response.body()?.articles as MutableList<Articles>
                    newsAdapter = NewsAdapter(articlesList, fragmentContext)
                    rvExploreNews?.adapter = newsAdapter

                    // Remove default background if the api response successfully
                    setFragmentBackground(false)

                    // Set-up news viewer if the api response successfully
                    setupNewsViewer()

                }
                if (!isNewLoad && response.body()?.articles != null){
                    newsAdapter.addItems(response.body()?.articles as MutableList<Articles>)
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) { }
        })
    }

    // METHOD TO SETUP INFINITE SCROLLING
    private fun setupInfiniteScroll(){
        rvExploreNews?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemsCount = linearLayoutManager.childCount - 1
                val pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()

                if (visibleItemsCount + pastVisibleItems == newsAdapter.itemCount){
                    if(articlesPage * 10 == newsAdapter.itemCount){
                        articlesPage++
                        getExploreNews(false, articlesCategory, articlesPage)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    // METHOD TO RESET VARIABLES THAT IS NEEDED FOR INFINITE SCROLL WORKING PROPERLY
    private fun resetInfiniteScroll(category: String){
        articlesPage = 1
        articlesCategory = category
        getExploreNews(true, articlesCategory, articlesPage)
    }

    // METHOD TO SETUP NEWS VIEWER
    private fun setupNewsViewer(){
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

    // METHOD TO CHANGE FRAGMENT BACKGROUND
    private fun setFragmentBackground(status: Boolean){
        when(status){
            true -> rvExploreNews!!.setBackgroundResource(R.drawable.home_background_loading)
            else -> rvExploreNews!!.setBackgroundResource(R.drawable.home_bg_white)
        }
    }

}