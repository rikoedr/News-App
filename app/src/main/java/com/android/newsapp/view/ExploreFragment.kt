package com.android.newsapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var rvExploreNews: RecyclerView? = null
    private lateinit var exploreNewsAdapter: NewsAdapter

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // INIT HEADLINES RECYCLERVIEW
        initExploreNewsRV()

        // GET DEFAULT EXPLORE NEWS
        getExploreNews(ContractAPI.TECH)

        // EXPLORE BAR
        exploreBar()
    }

    private fun initExploreNewsRV(){
        rvExploreNews = view?.findViewById(R.id.rv_explore_news)
        rvExploreNews?.layoutManager = LinearLayoutManager(context)
    }

    private fun getExploreNews(category: String){
        NewsClient.exploreAPI.getExploreNews(category, ContractAPI.API_KEY, ContractAPI.PAGE_SIZE_EXPLORE, 1).enqueue(object : Callback<NewsModel>{
            override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                // Troubleshooting
                Log.i("Explore News API", "Status Explore : API Connected \n Response : ${response.code()} ")
                Log.i("Explore News API", "Status Explore : ${response.body()?.status}")
                Log.i("Explore News API", "Status Explore : ${response.body()?.totalResults}")
                Log.i("Explore News API", "Status Explore : ${response.body()?.articles}")

                // Collect Data From API
                val exploreNewsModel: NewsModel? = response.body()
                if(exploreNewsModel != null){
                    if(exploreNewsModel.articles.isNotEmpty()){
                        // Set Explore News Articles
                        val exploreNewsArticles = exploreNewsModel.articles
                        exploreNewsAdapter = NewsAdapter(exploreNewsArticles, context)
                        rvExploreNews?.adapter = exploreNewsAdapter
                    }
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
            }
        })
    }

    private fun exploreBar(){
        val rgExplore = view?.findViewById<RadioGroup>(R.id.rg_explore_bar)

        rgExplore?.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_explore_health -> getExploreNews(ContractAPI.HEALTH)
                R.id.rb_explore_economy -> getExploreNews(ContractAPI.ECONOMY)
                R.id.rb_explore_sport -> getExploreNews(ContractAPI.SPORT)
                R.id.rb_explore_science -> getExploreNews(ContractAPI.SCIENCE)
                else -> getExploreNews(ContractAPI.TECH)
            }
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}