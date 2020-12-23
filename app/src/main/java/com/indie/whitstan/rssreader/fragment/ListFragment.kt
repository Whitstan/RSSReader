package com.indie.whitstan.rssreader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.ItemAdapter
import com.indie.whitstan.rssreader.databinding.FragmentRssListBinding
import com.indie.whitstan.rssreader.model.Item
import com.indie.whitstan.rssreader.model.RSSObject
import com.indie.whitstan.rssreader.network.RetrofitClient
import com.indie.whitstan.rssreader.util.MODE_FAVORITE
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

class ListFragment : Fragment() {

    private lateinit var binding: FragmentRssListBinding
    private lateinit var mItemViewModel: ItemViewModel
    private var mAdapter: ItemAdapter? = null
    private var mRetrofitClient: RetrofitClient = RetrofitClient()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ContentLoadingProgressBar
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rss_list,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        setupViewModel()
        loadRSSData()
    }

    override fun onResume() {
        super.onResume()
        refreshFavoriteMarkers()
    }

    private fun setupViews() {
        mRecyclerView = binding.rssItemsRecyclerView
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        mProgressBar = binding.progressBarRssItems
        mSwipeRefreshLayout = binding.swipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener {loadRSSData()}
    }

    private fun setupViewModel(){
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
    }

    private fun loadRSSData() {
        showLoadingIndicator()
        mRetrofitClient.createCall()?.enqueue(object : Callback<RSSObject?> {
            override fun onResponse(call: Call<RSSObject?>, response: Response<RSSObject?>) {
                hideLoadingIndicator()
                mAdapter = response.body()?.let {
                    it.items?.let { rssIt ->
                        ItemAdapter(mItemViewModel, rssIt)
                    }
                }!!
                mAdapter?.setMode(MODE_FAVORITE)
                mRecyclerView.adapter = mAdapter
                refreshFavoriteMarkers()
            }

            override fun onFailure(call: Call<RSSObject?>, error: Throwable) {
                hideLoadingIndicator()
                Toast.makeText(context, R.string.error_while_loading_data, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }

    private fun refreshFavoriteMarkers(){
        markFavoritesByList(mAdapter?.mItemsList)
        mAdapter?.notifyDataSetChanged()
        mAdapter?.closeAllSwipeRevealLayouts()
    }

    private fun markFavoritesByList(listOfItems: List<Item>?){
        if (listOfItems != null) {
            GlobalScope.launch(Dispatchers.IO) {
                for (item: Item in listOfItems) {
                    item.isFavorite = mItemViewModel.isRSSItemStoredInDB(item.guid)
                }
            }
        }
    }

    private fun showLoadingIndicator() {
        mRecyclerView.visibility = View.GONE
        mProgressBar.visibility = View.VISIBLE
        mSwipeRefreshLayout.isRefreshing = false
    }

    private fun hideLoadingIndicator() {
        mRecyclerView.visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        mSwipeRefreshLayout.isRefreshing = false
    }
}