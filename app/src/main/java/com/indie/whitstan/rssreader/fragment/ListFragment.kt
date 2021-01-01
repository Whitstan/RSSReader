package com.indie.whitstan.rssreader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_articles_list.*

import org.koin.android.viewmodel.ext.android.sharedViewModel

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.LocalArticleAdapter
import com.indie.whitstan.rssreader.databinding.FragmentArticlesListBinding
import com.indie.whitstan.rssreader.event.EventArticlesRefreshed
import com.indie.whitstan.rssreader.fragment.base.BaseFragment
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel
import de.greenrobot.event.EventBus

class ListFragment : BaseFragment(){

    private lateinit var binding: FragmentArticlesListBinding
    private val itemViewModel : ItemViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_articles_list,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        val adapter = LocalArticleAdapter(itemViewModel)
        rvArticlesList.adapter = adapter
        itemViewModel.getArticlesLiveData().observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            hideLoadingIndicator()
        })
        itemViewModel.loadDataFromDb()
    }

    override fun onStart() {
        super.onStart()
        showLoadingIndicator()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun setupViews() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvArticlesList.layoutManager = linearLayoutManager
        rvArticlesList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        srlArticlesList.setOnRefreshListener {itemViewModel.refreshArticlesData()}
    }

    private fun showLoadingIndicator() {
        rvArticlesList.visibility = View.GONE
        clpbArticlesList.visibility = View.VISIBLE
        srlArticlesList.isRefreshing = false
    }

    private fun hideLoadingIndicator() {
        rvArticlesList.visibility = View.VISIBLE
        clpbArticlesList.visibility = View.GONE
        srlArticlesList.isRefreshing = false
    }

    // EventBus events
    @Suppress("unused", "UNUSED_PARAMETER")
    fun onEventMainThread(event: EventArticlesRefreshed) {
        hideLoadingIndicator()
    }
}