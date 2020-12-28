package com.indie.whitstan.rssreader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import org.koin.android.ext.android.inject

import kotlinx.android.synthetic.main.fragment_articles_list.*

import org.koin.android.viewmodel.ext.android.sharedViewModel

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.ArticleAdapter
import com.indie.whitstan.rssreader.databinding.FragmentArticlesListBinding
import com.indie.whitstan.rssreader.persistence.ItemRepository
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel
import kotlinx.android.synthetic.main.fragment_favorites_list.*

class ListFragment : Fragment(){

    private lateinit var binding: FragmentArticlesListBinding
    private val itemViewModel : ItemViewModel by sharedViewModel()
    private val repository : ItemRepository by inject()

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
        setupViews()
        itemViewModel.articlesMediatorData.observe(viewLifecycleOwner, Observer {
            val adapter = ArticleAdapter()
            adapter.setItems(it)
            rvArticlesList.adapter = adapter
            hideLoadingIndicator()
        })
        itemViewModel.loadArticlesFromDb()
    }

    override fun onStart() {
        super.onStart()
        showLoadingIndicator()
    }

    private fun setupViews() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvArticlesList.layoutManager = linearLayoutManager
        rvArticlesList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        srlArticlesList.setOnRefreshListener {repository.fetchRssData(itemViewModel)}
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
}