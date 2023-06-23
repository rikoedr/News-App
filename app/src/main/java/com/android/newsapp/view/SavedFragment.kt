package com.android.newsapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapp.R
import com.android.newsapp.adapter.SavedAdapter
import com.android.newsapp.database.DBController
import com.android.newsapp.model.SavedModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SavedFragment : Fragment() {
    private var rvSaved: RecyclerView? = null
    private lateinit var savedAdapter: SavedAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var savedFragmentLayout: LinearLayout
    private lateinit var fragmentContext: Context

    private lateinit var dbController: DBController
    private lateinit var savedNewsList: MutableList<SavedModel>

    private var isNewLoad = true

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedFragmentLayout = view.findViewById(R.id.ll_saved_fragment)

        // INITIALIZE RECYCLERVIEW AND ADAPTER
        initSavedNewsRV()

        // INITIALIZE NEWS VIEWER
        setupNewsViewer()
    }

    override fun onResume() {
        super.onResume()

        if(isNewLoad){
            isNewLoad = false
        }else{
            initSavedNewsRV()
            setupNewsViewer()
        }
    }

    // METHOD TO INITIALIZE RECYCLERVIEW FOR 'SAVED NEWS'
    private fun initSavedNewsRV(){
        // Initialize RecyclerView and Adapters
        linearLayoutManager = LinearLayoutManager(fragmentContext)
        rvSaved = view?.findViewById(R.id.rv_saved_news)
        rvSaved?.layoutManager = linearLayoutManager

        dbController = DBController(fragmentContext)
        savedNewsList = dbController.getSavedNews()
        savedAdapter = SavedAdapter(savedNewsList, fragmentContext)
        rvSaved?.adapter = savedAdapter

        // Initialize Fragment Background
        when(savedNewsList.isEmpty()){
            true -> savedFragmentLayout.setBackgroundResource(R.drawable.saved_empty_background)
            else -> savedFragmentLayout.setBackgroundResource(R.drawable.home_bg_white)
        }
    }

    // METHOD TO SET-UP NEWSVIEWER
    private fun setupNewsViewer(){
        savedAdapter.setOnItemClickListener(object : SavedAdapter.OnSavedItemClickListener{
            override fun onSavedItemClick(position: Int, source: String, title: String, publishedAt: String, urlToOpen: String) {
                val intent = Intent(fragmentContext, NewsViewerActivity::class.java)
                intent.putExtra("articleSource", source)
                intent.putExtra("articleTitle", title)
                intent.putExtra("articlePublishedAt", publishedAt)
                intent.putExtra("urlToOpen", urlToOpen)
                startActivity(intent)
            }
        })
    }

}