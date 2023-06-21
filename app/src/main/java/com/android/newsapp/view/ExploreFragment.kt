package com.android.newsapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.NewsViewerActivity
import com.android.newsapp.R
import com.android.newsapp.adapter.NewsAdapter
import com.android.newsapp.api.ContractAPI
import com.android.newsapp.api.NewsClient
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

        // GET DEFAULT EXPLORE NEWS
        getExploreNews(true, ContractAPI.TECH, 1)

        // EXPLORE BAR
        setupExploreBar()

        // INFINITE SCROLLING
        rvExploreNews?.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemsCount = linearLayoutManager.childCount
                val pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()

                /*
                Log.i("check scroll", "Visible Items : $visibleItemsCount || Past Visible Items : $pastVisibleItems")
                Log.i("check scroll", "All Item View : ${visibleItemsCount+pastVisibleItems} || Adapter Item Count : ${newsAdapter.itemCount}")

                if (visibleItemsCount + pastVisibleItems == newsAdapter.itemCount){
                    if(articlesPage * 10 == newsAdapter.itemCount){
                        Log.i("check scroll", "UJUNG BOS!!")
                    }
                }*/

                super.onScrolled(recyclerView, dx, dy)
            }
        })


    }

    private fun getExploreNews(isNewLoad: Boolean, category: String, page: Int){
        NewsClient.exploreAPI.getExploreNews(category, ContractAPI.API_KEY, ContractAPI.PAGE_SIZE_EXPLORE, page).enqueue(object : Callback<NewsModel>{
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {

                /*
                if(isNewLoad){
                    val articlesList = response.body()?.articles as MutableList<Articles>
                    newsAdapter = NewsAdapter(articlesList, fragmentContext)
                    rvExploreNews?.adapter = newsAdapter }

                else{
                    newsAdapter.addItems(response.body()?.articles as MutableList<Articles>)
                }

                // Remove Background When Articles Loaded
                setBackground(false)

                // Setting Up News Viewer
                newsViewer()
                
                 */

            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
            }
        })
    }

    private fun setupExploreBar(){
        val rgExplore = view?.findViewById<RadioGroup>(R.id.rg_explore_bar)

        rgExplore?.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_explore_health -> getExploreNews(true, ContractAPI.HEALTH, 1)
                R.id.rb_explore_economy -> getExploreNews(true, ContractAPI.ECONOMY, 1)
                R.id.rb_explore_sport -> getExploreNews(true, ContractAPI.SPORT, 1)
                R.id.rb_explore_science -> getExploreNews(true, ContractAPI.SCIENCE, 1)
                else -> getExploreNews(true, ContractAPI.TECH, 1)
            }
        }
    }

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

    private fun setBackground(status: Boolean){
        when(status){
            true -> rvExploreNews!!.setBackgroundResource(R.drawable.home_background_loading)
            else -> rvExploreNews!!.setBackgroundResource(R.drawable.home_bg_white)
        }
    }

}