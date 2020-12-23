package com.indie.whitstan.rssreader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

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
import kotlinx.android.synthetic.main.fragment_rss_list.*

class ListFragment : Fragment(){

    private val mRetrofitClient: RetrofitClient by lazy { RetrofitClient() }

    private lateinit var binding: FragmentRssListBinding
    private lateinit var mItemViewModel: ItemViewModel

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

    private fun setupViews() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvRSSItems.layoutManager = linearLayoutManager
        rvRSSItems.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        swipe_refresh_layout.setOnRefreshListener {loadRSSData()}
    }

    private fun setupViewModel(){
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
    }

    private fun loadRSSData() {
        showLoadingIndicator()
        mRetrofitClient.createRSSApi().getRSSObject?.enqueue(object : Callback<RSSObject?> {
            override fun onResponse(call: Call<RSSObject?>, response: Response<RSSObject?>) {
                hideLoadingIndicator()
                rvRSSItems.adapter = response.body()?.let {
                    it.items?.let { rssIt ->
                        ItemAdapter(mItemViewModel, rssIt)
                    }
                }!!
                (rvRSSItems.adapter as ItemAdapter).setMode(MODE_FAVORITE)
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
        markFavoritesByList((rvRSSItems.adapter as ItemAdapter).mItemsList)
        rvRSSItems.adapter?.notifyDataSetChanged()
        (rvRSSItems.adapter as ItemAdapter).closeAllSwipeRevealLayouts()
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
        rvRSSItems.visibility = View.GONE
        clpbRSSItems.visibility = View.VISIBLE
        swipe_refresh_layout.isRefreshing = false
    }

    private fun hideLoadingIndicator() {
        rvRSSItems.visibility = View.VISIBLE
        clpbRSSItems.visibility = View.GONE
        swipe_refresh_layout.isRefreshing = false
    }
}