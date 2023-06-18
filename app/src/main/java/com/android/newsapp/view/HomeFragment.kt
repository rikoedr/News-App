package com.android.newsapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.adapter.NewsAdapter
import com.android.newsapp.adapter.TopNewsAdapter
import com.android.newsapp.api.ContractAPI
import com.android.newsapp.api.NewsClient
import com.android.newsapp.model.NewsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var rvHeadlines: RecyclerView? = null
    private lateinit var headlinesAdapter: NewsAdapter
    private var rvTopHeadlines: RecyclerView? = null
    private lateinit var topHeadlinesAdapter: TopNewsAdapter

    // BUILT-IN VARIABLE FRAGEMENTS
    private var param1: String? = null
    private var param2: String? = null

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

        // INIT HEADLINES RECYCLERVIEW
        initHeadlinesRV()

        // GET HEADLINES NEWS DATA
        getHeadlinesNews()
    }

    private fun initHeadlinesRV(){
        rvTopHeadlines = view?.findViewById(R.id.rv_top_headlines)
        rvTopHeadlines?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvHeadlines = view?.findViewById(R.id.rv_headlines)
        rvHeadlines?.layoutManager = LinearLayoutManager(context)
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
                        // Set Top Headlines Article
                        val topHeadlinesArticles = if (headlinesModel.articles.size > 5) headlinesModel.articles.take(5) else headlinesModel.articles
                        topHeadlinesAdapter = TopNewsAdapter(topHeadlinesArticles, context)
                        rvTopHeadlines?.adapter = topHeadlinesAdapter

                        // Set Headlines Article
                        val headlinesArticles = headlinesModel.articles.subList(5, headlinesModel.articles.size)
                        headlinesAdapter = NewsAdapter(headlinesArticles, context)
                        rvHeadlines?.adapter = headlinesAdapter
                    }
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {

            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}